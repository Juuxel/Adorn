package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.EmptyInventory
import net.minecraft.block.entity.BlockEntity
import net.minecraft.container.BlockContext
import net.minecraft.container.PropertyDelegate
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.BasicInventory
import net.minecraft.inventory.Inventory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BaseAdornController(
    syncId: Int,
    playerInv: PlayerInventory,
    context: BlockContext,
    blockInventory: Inventory,
    propertyDelegate: PropertyDelegate = getBlockPropertyDelegate(context)
) : CottonScreenController(
    null, syncId, playerInv, blockInventory, propertyDelegate
) {
    override fun canUse(player: PlayerEntity?) = true

    override fun getCraftingResultSlotIndex() = -1

    // TODO: Remove this
    override fun setPropertyDelegate(delegate: PropertyDelegate) = apply {
        this.propertyDelegate = delegate
    }

    companion object {
        fun getBlockEntity(context: BlockContext): BlockEntity? =
            context.run<BlockEntity?> { world: World, pos: BlockPos ->
                world.getBlockEntity(pos)
            }.orElse(null)

        fun getBlockInventoryOrCreate(context: BlockContext, fallbackSize: Int) =
            getBlockInventory(context).let {
                if (it is EmptyInventory)
                    when (fallbackSize) {
                        0 -> EmptyInventory.INSTANCE
                        else -> BasicInventory(fallbackSize)
                    }
                else it
            }
    }
}
