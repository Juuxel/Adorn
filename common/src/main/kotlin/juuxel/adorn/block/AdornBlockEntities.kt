package juuxel.adorn.block

import juuxel.adorn.block.entity.AdornBlockEntityType
import juuxel.adorn.block.entity.BlockEntityDescriptor
import juuxel.adorn.platform.BlockEntityBridge
import juuxel.adorn.platform.Registrar
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType

object AdornBlockEntities {
    val BLOCK_ENTITIES = Registrar.blockEntity()

    val SHELF: BlockEntityType<BlockEntity> by register("shelf", BlockEntityBridge.shelf())
    val DRAWER: BlockEntityType<BlockEntity> by register("drawer", BlockEntityBridge.drawer())
    val KITCHEN_CUPBOARD: BlockEntityType<BlockEntity> by register("kitchen_cupboard", BlockEntityBridge.kitchenCupboard())
    val TRADING_STATION: BlockEntityType<BlockEntity> by register("trading_station", BlockEntityBridge.tradingStation())

    private fun register(name: String, descriptor: BlockEntityDescriptor<*>) =
        BLOCK_ENTITIES.register(name) { AdornBlockEntityType(descriptor.factory) { descriptor.type.isInstance(it) } }

    fun init() {}
}
