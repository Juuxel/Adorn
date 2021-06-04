@file:JvmName("BlockEntityBridgeImpl")
package juuxel.adorn.platform.fabric

import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.TradingStationBlock
import juuxel.adorn.block.entity.BlockEntityDescriptor
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.DrawerBlockEntityFabric
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntityFabric
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntityFabric
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntityFabric

fun shelf(): BlockEntityDescriptor<ShelfBlock, ShelfBlockEntity> =
    BlockEntityDescriptor(::ShelfBlockEntityFabric)

fun drawer(): BlockEntityDescriptor<DrawerBlock, DrawerBlockEntity> =
    BlockEntityDescriptor(::DrawerBlockEntityFabric)

fun kitchenCupboard(): BlockEntityDescriptor<KitchenCupboardBlock, KitchenCupboardBlockEntity> =
    BlockEntityDescriptor(::KitchenCupboardBlockEntityFabric)

fun tradingStation(): BlockEntityDescriptor<TradingStationBlock, TradingStationBlockEntity> =
    BlockEntityDescriptor(::TradingStationBlockEntityFabric)
