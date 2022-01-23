package juuxel.adorn.util

import net.minecraft.util.Identifier

@Suppress("UNUSED")
abstract class ForgeRegistryEntryImpl(val registryType: Class<*>) {
    var registryName: Identifier? = null
        private set

    fun setRegistryName(registryName: Identifier?): Any {
        this.registryName = registryName
        return this
    }
}
