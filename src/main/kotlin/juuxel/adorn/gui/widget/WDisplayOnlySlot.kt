package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.GuiDescription
import io.github.cottonmc.cotton.gui.ValidatedSlot
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory

class WDisplayOnlySlot(private val inv: Inventory, private val slotIndex: Int) : WItemSlot(
    inv, slotIndex, 1, 1, false, false
) {
    override fun createPeers(c: GuiDescription) {
        c.addSlotPeer(Peer(inv, slotIndex, absoluteX, absoluteY))
    }

    private class Peer(inv: Inventory, index: Int, xPos: Int, yPos: Int) : ValidatedSlot(inv, index, xPos, yPos) {
        override fun canTakeItems(player: PlayerEntity?) = false
    }
}
