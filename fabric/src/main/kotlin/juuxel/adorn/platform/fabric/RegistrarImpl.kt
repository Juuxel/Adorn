@file:JvmName("RegistrarKtImpl")
package juuxel.adorn.platform.fabric

import juuxel.adorn.AdornCommon.id
import juuxel.adorn.lib.Registered
import juuxel.adorn.platform.Registrar
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

class RegistrarImpl<T>(private val registry: Registry<T>) : Registrar<T> {
    override fun <U : T> register(id: String, provider: () -> U): Registered<U> {
        val registered = Registry.register(registry, id(id), provider.invoke())
        return Registered { registered }
    }
}

fun blockRegistrar(): Registrar<Block> {
    return RegistrarImpl(Registry.BLOCK)
}

fun itemRegistrar(): Registrar<Item> {
    return RegistrarImpl(Registry.ITEM)
}

fun blockEntityRegistrar(): Registrar<BlockEntityType<*>> {
    return RegistrarImpl(Registry.BLOCK_ENTITY_TYPE)
}

fun entityRegistrar(): Registrar<EntityType<*>> {
    return RegistrarImpl(Registry.ENTITY_TYPE)
}

fun menuRegistrar(): Registrar<ScreenHandlerType<*>> {
    return RegistrarImpl(Registry.SCREEN_HANDLER)
}

fun soundEventRegistrar(): Registrar<SoundEvent> {
    return RegistrarImpl(Registry.SOUND_EVENT)
}
