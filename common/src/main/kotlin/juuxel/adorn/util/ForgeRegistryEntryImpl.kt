package juuxel.adorn.util

import net.minecraft.util.Identifier

@Suppress("UNUSED")
abstract class ForgeRegistryEntryImpl<V>(private val registryType: Class<V>) {
    private var registryName: Identifier? = null

    fun getRegistryName(): Identifier? = registryName

    fun setRegistryName(registryName: Identifier?): V {
        this.registryName = registryName
        @Suppress("UNCHECKED_CAST")
        return this as V
    }

    fun getRegistryType(): Class<V> = registryType
}
