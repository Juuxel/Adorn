package juuxel.adorn.platform.forge

import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.block.TradingStationBlock
import juuxel.adorn.block.entity.BlockEntityDescriptor
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.platform.BlockEntityDescriptors

object BlockEntityDescriptorsImpl : BlockEntityDescriptors() {
    override val drawer: BlockEntityDescriptor<DrawerBlock, DrawerBlockEntity> =
        BlockEntityDescriptor(::DrawerBlockEntity)

    override val kitchenCupboard: BlockEntityDescriptor<KitchenCupboardBlock, KitchenCupboardBlockEntity> =
        BlockEntityDescriptor(::KitchenCupboardBlockEntity)

    override val tradingStation: BlockEntityDescriptor<TradingStationBlock, TradingStationBlockEntity> =
        BlockEntityDescriptor(::TradingStationBlockEntity)
}
