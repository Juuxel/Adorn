@file:JvmName("BlockEntityBridgeImpl")
package juuxel.adorn.platform.fabric

import juuxel.adorn.block.entity.BlockEntityDescriptor
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity

fun shelf(): BlockEntityDescriptor<*> =
    BlockEntityDescriptor { ShelfBlockEntity() }

fun drawer(): BlockEntityDescriptor<*> =
    BlockEntityDescriptor { DrawerBlockEntity() }

fun kitchenCupboard(): BlockEntityDescriptor<*> =
    BlockEntityDescriptor { KitchenCupboardBlockEntity() }

fun tradingStation(): BlockEntityDescriptor<*> =
    BlockEntityDescriptor { TradingStationBlockEntity() }
