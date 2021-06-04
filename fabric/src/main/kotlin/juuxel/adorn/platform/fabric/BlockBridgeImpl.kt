package juuxel.adorn.platform.fabric

import juuxel.adorn.block.TableLampBlock
import juuxel.adorn.lib.AdornSounds
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.Material
import net.minecraft.block.TallPlantBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object BlockBridgeImpl {
    @JvmStatic
    fun copySettingsSafely(source: Block): AbstractBlock.Settings {
        val defaultState = source.defaultState
        return FabricBlockSettings.of(defaultState.material)
            .luminance(defaultState.luminance)
            .hardness(defaultState.getHardness(null, null))
            .resistance(source.blastResistance)
            .velocityMultiplier(source.velocityMultiplier)
            .jumpVelocityMultiplier(source.jumpVelocityMultiplier)
            .slipperiness(source.slipperiness)
            .sounds(defaultState.soundGroup)
    }

    @JvmStatic
    fun onTallBlockBrokenInCreative(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) =
        TallPlantBlock.onBreakInCreative(world, pos, state, player)

    @JvmStatic
    fun createTableLampSettings(color: DyeColor?): AbstractBlock.Settings =
        FabricBlockSettings.of(Material.REDSTONE_LAMP, color)
            .hardness(0.3f)
            .resistance(0.3f)
            .sounds(BlockSoundGroup.WOOL)
            .luminance { state: BlockState -> if (state.get(TableLampBlock.LIT)) 15 else 0 }

    @JvmStatic
    fun createGroundStoneTorchSettings(): AbstractBlock.Settings =
        FabricBlockSettings.copyOf(Blocks.TORCH)
            .sounds(BlockSoundGroup.STONE)
            .luminance(15)

    @JvmStatic
    fun createWallStoneTorchSettings(groundStoneTorch: () -> Block): AbstractBlock.Settings {
        val torch = groundStoneTorch()
        return FabricBlockSettings.copyOf(torch).dropsLike(torch)
    }

    @JvmStatic
    fun createChainLinkFenceSettings(): AbstractBlock.Settings =
        FabricBlockSettings.copyOf(Blocks.IRON_BARS)
            .sounds(AdornSounds.CHAIN_LINK_FENCE)

    @JvmStatic
    fun createStoneLadderSettings(): AbstractBlock.Settings =
        FabricBlockSettings.copyOf(Blocks.STONE)
            .breakByTool(FabricToolTags.PICKAXES)
            .nonOpaque()
}
