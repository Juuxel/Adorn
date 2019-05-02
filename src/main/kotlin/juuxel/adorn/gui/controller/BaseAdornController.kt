package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import net.minecraft.block.entity.BlockEntity
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.TranslatableTextComponent
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BaseAdornController(syncId: Int, playerInv: PlayerInventory, context: BlockContext, invWidth: Int, invHeight: Int) : CottonScreenController(
    null, syncId, playerInv, getBlockInventory(context), getBlockPropertyDelegate(context)
) {
    // For inventories with no stored inventory slots
    constructor(syncId: Int, playerInv: PlayerInventory, context: BlockContext) : this(syncId, playerInv, context, 0, 0)

    init {
        (rootPanel as WGridPanel).apply {
            add(
                WLabel(
                    TranslatableTextComponent(
                        context.run<String> { world, pos -> world.getBlockState(pos).block.translationKey }.get()
                    ), WLabel.DEFAULT_TEXT_COLOR
                ), 0, 0
            )

            for (row in 0 until invHeight) {
                for (col in 0 until invWidth) {
                    val hasEvenWidth = invWidth % 2 == 0
                    // Creates a gap for even-width inventories, looks really weird
                    val xOffset =
                        if (hasEvenWidth && col + 1 > invWidth / 2) 1
                        else 0
                    val slot = WItemSlot.of(blockInventory, col + row * invWidth)
                    add(slot, col + (9 - invWidth) / 2 + xOffset, row + 1)
                }
            }

            if (invHeight > 0) {
                add(createPlayerInventoryPanel(), 0, 2 + invHeight)
            }

            validate(this@BaseAdornController)
        }
    }

    override fun canUse(player: PlayerEntity?) = true

    override fun getCraftingResultSlotIndex() = -1

    companion object {
        fun getBlockEntity(context: BlockContext): BlockEntity? =
            context.run<BlockEntity?> { world: World, pos: BlockPos ->
                world.getBlockEntity(pos)
            }.orElse(null)
    }
}
