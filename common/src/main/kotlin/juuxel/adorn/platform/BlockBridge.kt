package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object BlockBridge {
    /**
     * Creates a safe copy of this block's settings.
     *
     * The safe copy does not have lambdas that reference this block directly.
     * Instead, the default state is used for the various lambdas.
     */
    @JvmStatic
    @ExpectPlatform
    fun copySettingsSafely(source: Block): AbstractBlock.Settings = expected

    // AW on Fabric, accessor on Forge
    @JvmStatic
    @ExpectPlatform
    fun onTallBlockBrokenInCreative(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity): Unit =
        expected

    @JvmStatic
    @ExpectPlatform
    fun createTableLampSettings(color: DyeColor?): AbstractBlock.Settings = expected

    @JvmStatic
    @ExpectPlatform
    fun createGroundStoneTorchSettings(): AbstractBlock.Settings = expected

    @JvmStatic
    @ExpectPlatform
    fun createWallStoneTorchSettings(groundStoneTorch: () -> Block): AbstractBlock.Settings = expected

    @JvmStatic
    @ExpectPlatform
    fun createChainLinkFenceSettings(): AbstractBlock.Settings = expected

    @JvmStatic
    @ExpectPlatform
    fun createStoneLadderSettings(): AbstractBlock.Settings = expected
}
