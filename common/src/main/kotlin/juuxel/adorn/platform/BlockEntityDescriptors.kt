package juuxel.adorn.platform

import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.block.KitchenSinkBlock
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.TradingStationBlock
import juuxel.adorn.block.entity.BlockEntityDescriptor
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.KitchenSinkBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity

abstract class BlockEntityDescriptors {
    val shelf: BlockEntityDescriptor<ShelfBlock, ShelfBlockEntity> =
        BlockEntityDescriptor(::ShelfBlockEntity)

    abstract val drawer: BlockEntityDescriptor<DrawerBlock, DrawerBlockEntity>
    abstract val kitchenCupboard: BlockEntityDescriptor<KitchenCupboardBlock, KitchenCupboardBlockEntity>
    abstract val kitchenSink: BlockEntityDescriptor<KitchenSinkBlock, KitchenSinkBlockEntity>
    abstract val tradingStation: BlockEntityDescriptor<TradingStationBlock, TradingStationBlockEntity>
}
