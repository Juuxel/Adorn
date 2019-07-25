package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import juuxel.adorn.gui.painter.Painters
import juuxel.adorn.gui.widget.CenteredLabelWidget
import juuxel.adorn.util.Colors.WHITE
import juuxel.adorn.util.color
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.container.BlockContext
import net.minecraft.container.SlotActionType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.BasicInventory
import net.minecraft.item.ItemStack
import net.minecraft.text.TranslatableText

// TODO: Combine the two trading station guis
class TradingStationCustomerController(
    syncId: Int,
    playerInv: PlayerInventory,
    private val blockContext: BlockContext
) : BaseAdornController(
    syncId,
    playerInv,
    blockContext,
    BasicInventory(0),
    getBlockPropertyDelegate(blockContext)
) {
    // TODO: Remove
    private val slots = ArrayList<WItemSlot>()

    init {
        (rootPanel as WGridPanel).apply {
            add(
                WLabel(
                    TranslatableText(
                        blockContext.run<String> { world, pos -> world.getBlockState(pos).block.translationKey }.get()
                    ), titleColor
                ), 0, 0
            )

            val tradeInv = TradingStationController.getTrade(blockContext).createInventory()
            val storage = TradingStationController.getStorage(blockContext)

            fun WItemSlot.addToSlots() = apply { slots += this }

            add(WItemSlot.of(tradeInv, 0).addToSlots(), 1, 2)
            add(WItemSlot.of(tradeInv, 1).addToSlots(), 1, 4)

            add(CenteredLabelWidget(TranslatableText("block.adorn.trading_station.selling"), WHITE), 1, 1)
            add(CenteredLabelWidget(TranslatableText("block.adorn.trading_station.price"), WHITE), 1, 3)

            add(WItemSlot.of(storage, 0, 4, 3).addToSlots(), 3, 2)

            add(createPlayerInventoryPanel(), 0, 6)
            validate(this@TradingStationCustomerController)
        }
    }

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType, player: PlayerEntity): ItemStack {
        val slot = slotList.getOrNull(slotNumber)
        val cursorStack = player.inventory.cursorStack

        // Only allow modifying the player inventory
        return if (slot?.inventory is PlayerInventory) {
            super.onSlotClick(slotNumber, button, action, player)
        } else cursorStack
    }

    @Environment(EnvType.CLIENT)
    override fun addPainters() {
        super.addPainters()
        rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(color(0x359668)))
        slots.forEach { it.setBackgroundPainter(Painters.LIBGUI_STYLE_SLOT) }
    }

    override fun getTitleColor() = WHITE
}
