package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.EmptyInventory
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WPlayerInvPanel
import juuxel.adorn.client.gui.painter.Painters
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

abstract class BaseAdornController(
    syncId: Int,
    playerInv: PlayerInventory,
    context: ScreenHandlerContext,
    blockInventory: Inventory,
    propertyDelegate: PropertyDelegate = getBlockPropertyDelegate(context)
) : SyncedGuiDescription(
    syncId, playerInv, blockInventory, propertyDelegate
) {
    protected val playerInvPanel: WPlayerInvPanel by lazy { createPlayerInventoryPanel() }

    override fun canUse(player: PlayerEntity?) = true

    @Environment(EnvType.CLIENT)
    override fun addPainters() {
        playerInvPanel.backgroundPainter = Painters.LIBGUI_STYLE_SLOT
    }

    companion object {
        private val LOGGER = LogManager.getLogger()

        fun getBlock(context: ScreenHandlerContext) =
            context.run<Block> { world: World, pos: BlockPos ->
                world.getBlockState(pos).block
            }.orElse(Blocks.AIR)

        fun getBlockEntity(context: ScreenHandlerContext): BlockEntity? =
            context.run<BlockEntity?> { world: World, pos: BlockPos ->
                world.getBlockEntity(pos)
            }.orElse(null)

        fun getBlockInventoryOrCreate(context: ScreenHandlerContext, fallbackSize: Int) =
            getBlockInventory(context).let {
                if (it is EmptyInventory)
                    when (fallbackSize) {
                        0 -> EmptyInventory.INSTANCE
                        else -> {
                            LOGGER.warn(
                                "[Adorn] No block inventory found at {}",
                                context.run<BlockPos> { _, pos -> pos }
                                    .map(BlockPos::toString)
                                    .orElse("missing position")
                            )
                            SimpleInventory(fallbackSize)
                        }
                    } else it
            }
    }
}
