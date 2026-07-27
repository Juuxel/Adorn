package juuxel.adorn.gradle.util.zip;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public abstract class AsmStep implements ZipTransformer.Step {
    @Override
    public boolean shouldApply(String name) {
        return name.endsWith(".class");
    }

    @Override
    public final byte[] transform(String name, byte[] input, ZipTransformer.Filer filer) throws IOException {
        // Read the existing class
        ClassReader cr = new ClassReader(input);
        var node = new ClassNode();
        cr.accept(node, 0);

        // Transform
        boolean modified = transform(filer, node);
        if (!modified) return input;

        // Write out the new class
        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
        node.accept(cw);
        return cw.toByteArray();
    }

    protected abstract boolean transform(ZipTransformer.Filer filer, ClassNode classNode) throws IOException;
}
