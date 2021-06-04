package juuxel.adorn.platform

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface BlockBridge {
    /**
     * Creates a safe copy of this block's settings.
     *
     * The safe copy does not have lambdas that reference this block directly.
     * Instead, the default state is used for the various lambdas.
     */
    fun copySettingsSafely(source: Block): AbstractBlock.Settings

    // AW on Fabric, accessor on Forge
    fun onTallBlockBrokenInCreative(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity)

    // Block settings
    fun createTableLampSettings(color: DyeColor?): AbstractBlock.Settings
    fun createGroundStoneTorchSettings(): AbstractBlock.Settings
    fun createWallStoneTorchSettings(groundStoneTorch: () -> Block): AbstractBlock.Settings
    fun createChainLinkFenceSettings(): AbstractBlock.Settings
    fun createStoneLadderSettings(): AbstractBlock.Settings
}
