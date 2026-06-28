package juuxel.adorn.gradle.datagen;

import com.google.gson.Gson;
import juuxel.adorn.gradle.util.JarView;
import net.fabricmc.mappingio.MappingReader;
import net.fabricmc.mappingio.tree.MemoryMappingTree;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class GenerateEnumVerificationData extends DefaultTask {
    @InputFiles
    @PathSensitive(PathSensitivity.NONE)
    public abstract ConfigurableFileCollection getModJars();

    @Classpath
    public abstract ConfigurableFileCollection getMinecraftJars();

    @InputFile
    @Optional
    @PathSensitive(PathSensitivity.NONE)
    public abstract RegularFileProperty getMappings();

    @Input
    @Optional
    public abstract Property<String> getRuntimeNamespace();

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    @TaskAction
    protected void generate() throws IOException {
        Path output = getOutputFile().get().getAsFile().toPath();
        @Nullable Path mappingFile = getMappings().isPresent() ? getMappings().get().getAsFile().toPath() : null;
        @Nullable String runtimeNs = getRuntimeNamespace().getOrNull();

        try (var modView = JarView.open(getModJars()); var mcView = JarView.open(getMinecraftJars())) {
            generate(modView, mcView, mappingFile, runtimeNs, output);
        }
    }

    private static void generate(JarView modView, JarView mcView, @Nullable Path mappingFile, @Nullable String runtimeNs, Path output) throws IOException {
        Set<Type> types = new HashSet<>();
        var classes = modView.walkFiles()
            .filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".class"));

        try (classes) {
            var iter = classes.iterator();
            while (iter.hasNext()) {
                var path = iter.next();
                ClassNode cn = new ClassNode();
                ClassReader cr = new ClassReader(Files.readAllBytes(path));
                cr.accept(cn, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                types.addAll(processClass(cn));
            }
        }

        if (types.isEmpty()) return;
        Map<String, List<String>> allEnumData = new TreeMap<>();
        var mappings = new MemoryMappingTree();
        int namedNsId = -1;

        if (mappingFile != null) {
            mappings.setIndexByDstNames(true);
            MappingReader.read(mappingFile, mappings);
            namedNsId = mappings.getNamespaceId("named");
        }

        for (Type type : types) {
            Path classpathClass = mcView.getPath(type.getInternalName() + ".class");
            var cn = new ClassNode();
            var cr = new ClassReader(Files.readAllBytes(classpathClass));
            cr.accept(cn, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            List<String> entries = new ArrayList<>();
            for (FieldNode field : cn.fields) {
                if ((field.access & Opcodes.ACC_ENUM) != 0) {
                    entries.add(field.name);
                }
            }

            String className = runtimeNs != null ? mappings.getClass(cn.name, namedNsId).getName(runtimeNs) : cn.name;
            allEnumData.put(className.replace('/', '.'), entries);
        }

        Gson gson = new Gson();
        try (Writer writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
            gson.toJson(allEnumData, writer);
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Type> processClass(ClassNode cn) {
        if (cn.invisibleAnnotations == null || cn.invisibleAnnotations.isEmpty()) return List.of();

        for (AnnotationNode annotation : cn.invisibleAnnotations) {
            if (!"Ljuuxel/adorn/util/verification/UsesBlockProperty;".equals(annotation.desc)) continue;
            var values = annotation.values;
            for (int i = 0; i + 1 < values.size(); i += 2) {
                var name = values.get(i);
                if (!"value".equals(name)) continue;
                return (List<Type>) values.get(i + 1);
            }
        }

        return List.of();
    }
}
