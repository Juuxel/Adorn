package juuxel.adorn.platform.fabric;

import juuxel.adorn.block.TableLampBlock;
import juuxel.adorn.lib.AdornSounds;
import kotlin.jvm.functions.Function0;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class BlockBridgeImpl {
    public static AbstractBlock.Settings copySettingsSafely(Block source) {
        BlockState defaultState = source.getDefaultState();
        return FabricBlockSettings.of(defaultState.getMaterial())
            .luminance(defaultState.getLuminance())
            .hardness(defaultState.getHardness(null, null))
            .resistance(source.getBlastResistance())
            .velocityMultiplier(source.getVelocityMultiplier())
            .jumpVelocityMultiplier(source.getJumpVelocityMultiplier())
            .slipperiness(source.getSlipperiness())
            .sounds(defaultState.getSoundGroup());
    }

    public static void onTallBlockBrokenInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TallPlantBlock.onBreakInCreative(world, pos, state, player);
    }

    public static AbstractBlock.Settings createTableLampSettings(DyeColor color) {
        return FabricBlockSettings.of(Material.REDSTONE_LAMP, color)
            .hardness(0.3f)
            .resistance(0.3f)
            .sounds(BlockSoundGroup.WOOL)
            .luminance(state -> state.get(TableLampBlock.Companion.getLIT()) ? 15 : 0);
    }

    public static AbstractBlock.@NotNull Settings createGroundStoneTorchSettings() {
        return FabricBlockSettings.copyOf(Blocks.TORCH).sounds(BlockSoundGroup.STONE).luminance(15);
    }

    public static AbstractBlock.@NotNull Settings createWallStoneTorchSettings(Function0<Block> groundStoneTorch) {
        Block torch = groundStoneTorch.invoke();
        return FabricBlockSettings.copyOf(torch).dropsLike(torch);
    }

    public static AbstractBlock.@NotNull Settings createChainLinkFenceSettings() {
        return FabricBlockSettings.copyOf(Blocks.IRON_BARS).sounds(AdornSounds.INSTANCE.getCHAIN_LINK_FENCE());
    }

    public static AbstractBlock.@NotNull Settings createStoneLadderSettings() {
        return FabricBlockSettings.copyOf(Blocks.STONE).breakByTool(FabricToolTags.PICKAXES).nonOpaque();
    }
}
