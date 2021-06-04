@file:JvmName("BlockEntityBridgeImpl")
package juuxel.adorn.platform.fabric

import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.TradingStationBlock
import juuxel.adorn.block.entity.BlockEntityDescriptor
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity

fun shelf(): BlockEntityDescriptor<*> =
    BlockEntityDescriptor<ShelfBlock>(::ShelfBlockEntity)

fun drawer(): BlockEntityDescriptor<*> =
    BlockEntityDescriptor<DrawerBlock>(::DrawerBlockEntity)

fun kitchenCupboard(): BlockEntityDescriptor<*> =
    BlockEntityDescriptor<KitchenCupboardBlock>(::KitchenCupboardBlockEntity)

fun tradingStation(): BlockEntityDescriptor<*> =
    BlockEntityDescriptor<TradingStationBlock>(::TradingStationBlockEntity)
