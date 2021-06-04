package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.TradingStationBlock
import juuxel.adorn.block.entity.BlockEntityDescriptor
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity

object BlockEntityBridge {
    @ExpectPlatform
    @JvmStatic
    fun shelf(): BlockEntityDescriptor<ShelfBlock, ShelfBlockEntity> = expected

    @ExpectPlatform
    @JvmStatic
    fun drawer(): BlockEntityDescriptor<DrawerBlock, DrawerBlockEntity> = expected

    @ExpectPlatform
    @JvmStatic
    fun kitchenCupboard(): BlockEntityDescriptor<KitchenCupboardBlock, KitchenCupboardBlockEntity> = expected

    @ExpectPlatform
    @JvmStatic
    fun tradingStation(): BlockEntityDescriptor<TradingStationBlock, TradingStationBlockEntity> = expected
}
