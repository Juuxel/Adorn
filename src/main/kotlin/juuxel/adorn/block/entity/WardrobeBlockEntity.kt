package juuxel.adorn.block.entity

import juuxel.adorn.block.WardrobeBlock
import juuxel.adorn.gui.controller.WardrobeController
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.DefaultedList
import org.apache.logging.log4j.LogManager

class WardrobeBlockEntity : BaseInventoryBlockEntity(WardrobeBlock.BLOCK_ENTITY_TYPE, 25) {
    private val isBottom: Boolean by lazy { cachedState[WardrobeBlock.HALF] == DoubleBlockHalf.LOWER }

    private val bottom: WardrobeBlockEntity? by lazy {
        if (isBottom) return@lazy null

        world?.getBlockEntity(pos.down()) as? WardrobeBlockEntity
            ?: run {
                LOGGER.warn("Could not find bottom part of wardrobe block entity")
                null
            }
    }

    override fun getInvStackList(): DefaultedList<ItemStack> =
        if (isBottom) super.getInvStackList() else bottom!!.getInvStackList()

    override fun createContainer(syncId: Int, playerInv: PlayerInventory) =
        WardrobeController(syncId, playerInv, BlockContext.create(world, pos), displayName)

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}
