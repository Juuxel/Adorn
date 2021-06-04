package juuxel.adorn.platform

import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.sound.SoundEvent

interface RegistrarFactory {
    fun block(): Registrar<Block>
    fun item(): Registrar<Item>
    fun blockEntity(): Registrar<BlockEntityType<*>>
    fun entity(): Registrar<EntityType<*>>
    fun menu(): Registrar<ScreenHandlerType<*>>
    fun soundEvent(): Registrar<SoundEvent>
}
