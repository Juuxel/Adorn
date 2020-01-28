package juuxel.adorn.block

import juuxel.adorn.Adorn
import juuxel.adorn.block.entity.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry

object AdornBlockEntities {
    val SHELF: BlockEntityType<ShelfBlockEntity> = register("shelf", ::ShelfBlockEntity)
    val DRAWER: BlockEntityType<DrawerBlockEntity> = register("drawer", ::DrawerBlockEntity)
    val KITCHEN_CUPBOARD: BlockEntityType<KitchenCupboardBlockEntity> = register("kitchen_cupboard", ::KitchenCupboardBlockEntity)
    val TRADING_STATION: BlockEntityType<TradingStationBlockEntity> = register("trading_station", ::TradingStationBlockEntity)

    private fun <T : BlockEntity> register(name: String, supplier: () -> T): BlockEntityType<T> =
        Registry.register(Registry.BLOCK_ENTITY_TYPE, Adorn.id(name), MutableBlockEntityType(supplier))

    fun init() {}
}
