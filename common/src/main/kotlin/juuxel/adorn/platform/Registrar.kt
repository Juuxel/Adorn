package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import juuxel.adorn.lib.Registered
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.sound.SoundEvent

interface Registrar<T> {
    fun <U : T> register(id: String, provider: () -> U): Registered<U>
    fun <U : T> registerOptional(id: String, provider: () -> U?): Registered<U?> {
        val value: U = provider.invoke() ?: return Registered { null }
        return register(id) { value }
    }
}

@ExpectPlatform
fun blockRegistrar(): Registrar<Block> = expected

@ExpectPlatform
fun itemRegistrar(): Registrar<Item> = expected

@ExpectPlatform
fun blockEntityRegistrar(): Registrar<BlockEntityType<*>> = expected

@ExpectPlatform
fun entityRegistrar(): Registrar<EntityType<*>> = expected

@ExpectPlatform
fun menuRegistrar(): Registrar<ScreenHandlerType<*>> = expected

@ExpectPlatform
fun soundEventRegistrar(): Registrar<SoundEvent> = expected
