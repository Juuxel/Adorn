package juuxel.adorn.block

import juuxel.adorn.block.entity.AdornBlockEntityType
import juuxel.adorn.block.entity.BlockEntityDescriptor
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.platform.BlockEntityBridge
import juuxel.adorn.platform.blockEntityRegistrar
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType

object AdornBlockEntities {
    val BLOCK_ENTITIES = blockEntityRegistrar()

    val SHELF: BlockEntityType<ShelfBlockEntity> by register("shelf", BlockEntityBridge.shelf())
    val DRAWER: BlockEntityType<DrawerBlockEntity> by register("drawer", BlockEntityBridge.drawer())
    val KITCHEN_CUPBOARD: BlockEntityType<KitchenCupboardBlockEntity> by register("kitchen_cupboard", BlockEntityBridge.kitchenCupboard())
    val TRADING_STATION: BlockEntityType<TradingStationBlockEntity> by register("trading_station", BlockEntityBridge.tradingStation())

    private fun <E : BlockEntity> register(name: String, descriptor: BlockEntityDescriptor<*, E>) =
        BLOCK_ENTITIES.register(name) { AdornBlockEntityType(descriptor.factory) { descriptor.type.isInstance(it) } }

    fun init() {}
}
