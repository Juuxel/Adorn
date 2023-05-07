package juuxel.adorn.lens.processor;

import juuxel.adorn.lens.WithLens;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor14;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@SupportedAnnotationTypes("juuxel.adorn.lens.WithLens")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public final class LensProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, Entry.ClassEntry> topLevelEntries = new LinkedHashMap<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(WithLens.class)) {
            element.accept(new SimpleElementVisitor14<>() {
                @Override
                public Object visitVariable(VariableElement e, Object o) {
                    if (e.getKind() == ElementKind.FIELD) {
                        Entry.ClassEntry parent = e.getEnclosingElement().accept(new SimpleElementVisitor14<Entry.ClassEntry, Void>() {
                            @Override
                            public Entry.ClassEntry visitType(TypeElement e, Void v) {
                                // Try to find outer class
                                Entry.ClassEntry outer = e.getEnclosingElement().accept(this, null);

                                if (outer != null) {
                                    return outer.tryAddChild(e.getSimpleName().toString(),
                                        n -> new Entry.ClassEntry(n, e));
                                }

                                // If outer wasn't found, this is the top level
                                String typeName = processingEnv.getElementUtils().getBinaryName(e).toString();
                                return topLevelEntries.computeIfAbsent(typeName,
                                    n -> new Entry.ClassEntry(n, e));
                            }
                        }, null);

                        Entry.FieldEntry entry = new Entry.FieldEntry(e.getSimpleName().toString(), e.asType());
                        parent.addChild(entry);
                    }

                    return null;
                }
            }, null);
        }

        for (Entry.ClassEntry value : topLevelEntries.values()) {
            StringBuilder sb = new StringBuilder();
            String className = writeLenses(sb, 0, value);

            try {
                JavaFileObject file = processingEnv.getFiler().createSourceFile(className, value.type());
                try (Writer w = file.openWriter()) {
                    w.write(sb.toString());
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return true;
    }

    private String writeLenses(StringBuilder out, int nesting, Entry.ClassEntry entry) {
        Elements elements = processingEnv.getElementUtils();

        if (nesting == 0) { // top-level
            out.append("package ")
                .append(elements.getPackageOf(entry.type()).getQualifiedName())
                .append(';');

            out.append("\n\n");
            out.append("import juuxel.adorn.lens.Lens;");
            out.append("\n\n");
        }

        indent(out, nesting);
        out.append("public ");

        if (nesting > 0) {
            out.append("static ");
        }

        String className = entry.type().getSimpleName().toString();

        if (nesting == 0) {
            className += "Lenses";
        }

        out.append("final class ")
            .append(className)
            .append(" {\n");

        for (Entry child : entry.children().values()) {
            if (child instanceof Entry.ClassEntry ce) {
                writeLenses(out, nesting + 1, ce);
            } else {
                Entry.FieldEntry fe = (Entry.FieldEntry) child;

                String boxedType = switch (fe.type().getKind()) {
                    case BOOLEAN -> "java.lang.Boolean";
                    case BYTE -> "java.lang.Byte";
                    case SHORT -> "java.lang.Short";
                    case INT -> "java.lang.Integer";
                    case LONG -> "java.lang.Long";
                    case CHAR -> "java.lang.Character";
                    case FLOAT -> "java.lang.Float";
                    case DOUBLE -> "java.lang.Double";
                    default -> fe.type().toString();
                };

                indent(out, nesting + 1);
                out.append("public static final Lens<")
                    .append(entry.type().getQualifiedName())
                    .append(", ")
                    .append(boxedType)
                    .append("> ")
                    .append(fe.name())
                    .append(" = new Lens.Simple<>(source -> source.")
                    .append(fe.name())
                    .append(", (source, value) -> source.")
                    .append(fe.name())
                    .append(" = value);\n");
            }
        }

        indent(out, nesting);
        out.append("}\n");

        return elements.getPackageOf(entry.type()).getQualifiedName() + "." + className;
    }

    private void indent(StringBuilder out, int level) {
        out.append("    ".repeat(level));
    }

    private sealed interface Entry {
        String name();

        record ClassEntry(String name, TypeElement type, Map<String, Entry> children) implements Entry {
            ClassEntry(String name, TypeElement type) {
                this(name, type, new LinkedHashMap<>());
            }

            @SuppressWarnings("unchecked")
            <E extends Entry> E tryAddChild(String name, Function<String, E> child) {
                return (E) children.computeIfAbsent(name, child);
            }

            void addChild(Entry child) {
                children.put(child.name(), child);
            }
        }

        record FieldEntry(String name, TypeMirror type) implements Entry {
        }
    }
}
