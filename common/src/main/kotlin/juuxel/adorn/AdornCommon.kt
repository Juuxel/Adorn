package juuxel.adorn

import net.minecraft.util.Identifier

object AdornCommon {
    const val NAMESPACE = "adorn"

    @JvmStatic
    fun id(path: String) = Identifier(NAMESPACE, path)
}
