package juuxel.adorn.asm

import org.gradle.api.Action
import org.gradle.api.Task
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.TypeInsnNode
import java.nio.file.Files

object InlineServiceLoaderAction : Action<Task> {
    private val INLINE_PATTERN = MethodBodyPattern(
        listOf(
            InsnPattern(LdcInsnNode::class.java) { it.cst is Type },
            InsnPattern(MethodInsnNode::class.java) {
                it.opcode == Opcodes.INVOKESTATIC &&
                    it.owner == "java/util/ServiceLoader" &&
                    it.name == "load" &&
                    it.desc == "(Ljava/lang/Class;)Ljava/util/ServiceLoader;"
            },
            InsnPattern(MethodInsnNode::class.java) {
                it.opcode == Opcodes.INVOKEVIRTUAL &&
                    it.owner == "java/util/ServiceLoader" &&
                    it.name == "findFirst" &&
                    it.desc == "()Ljava/util/Optional;"
            },
        )
    )

    override fun execute(t: Task) {
        transformJar(t.outputs.files.singleFile.toPath()) { filer, classNode ->
            if ((classNode.invisibleAnnotations ?: emptyList()).none { it.desc == "Ljuuxel/adorn/util/InlineServices;" }) {
                // Skip classes without the anno
                return@transformJar
            }

            for (method in classNode.methods) {
                val success = INLINE_PATTERN.match(method) { ctx ->
                    val ldc = ctx.instructions.first() as LdcInsnNode
                    val type = (ldc.cst as Type).internalName.replace('/', '.')
                    val serviceFile = filer("META-INF/services/$type")
                    if (Files.exists(serviceFile)) {
                        val implType = Files.readAllLines(serviceFile).single()
                        ctx.replaceWith(
                            listOf(
                                TypeInsnNode(Opcodes.NEW, implType),
                                InsnNode(Opcodes.DUP),
                                MethodInsnNode(Opcodes.INVOKESPECIAL, implType, "<init>", "()V"),
                                MethodInsnNode(
                                    Opcodes.INVOKESTATIC,
                                    "java/util/Optional",
                                    "of",
                                    "(Ljava/lang/Object;)Ljava/util/Optional;"
                                ),
                            )
                        )
                    }
                }

                if (success) {
                    method.maxStack++
                }
            }
        }
    }
}
