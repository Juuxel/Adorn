package juuxel.adorn.platform.forge.test

import juuxel.adorn.util.ForgeRegistryEntryImpl
import net.minecraftforge.registries.IForgeRegistryEntry

// Checks that ForgeRegistryEntryImpl properly implements IForgeRegistryEntry
// by successfully compiling.
@Suppress("UNUSED")
class ForgeRegistryEntryImplCheck<V>(type: Class<V>) : ForgeRegistryEntryImpl<V>(type), IForgeRegistryEntry<V>
