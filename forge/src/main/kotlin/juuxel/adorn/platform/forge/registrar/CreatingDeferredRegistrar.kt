package juuxel.adorn.platform.forge.registrar

import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.util.Identifier
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.NewRegistryEvent
import net.minecraftforge.registries.RegistryBuilder

class CreatingDeferredRegistrar<T>(registry: RegistryKey<out Registry<T>>, private val defaultId: Identifier) : DeferredRegistrar<T>(registry) {
    private val key = registry

    override fun hook(modBus: IEventBus) {
        modBus.register(this)
        super.hook(modBus)
    }

    @SubscribeEvent
    fun createRegistry(event: NewRegistryEvent) {
        val registry = RegistryBuilder<T>()
            .setName(key.value)
            .setDefaultKey(defaultId)
        event.create(registry)
    }
}
