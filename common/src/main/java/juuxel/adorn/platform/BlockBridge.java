package juuxel.adorn.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import kotlin.jvm.functions.Function0;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public final class BlockBridge {
    /**
     * Creates a safe copy of this block's settings.
     *
     * <p>The safe copy does not have lambdas that reference this block directly.
     * Instead, the default state is used for the various lambdas.
     */
    @ExpectPlatform
    @NotNull
    public static AbstractBlock.Settings copySettingsSafely(Block source) {
        return PlatformCore.expected();
    }

    // AW on Fabric, accessor on Forge
    @ExpectPlatform
    public static void onTallBlockBrokenInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static AbstractBlock.Settings createTableLampSettings(DyeColor color) {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static AbstractBlock.Settings createGroundStoneTorchSettings() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static AbstractBlock.Settings createWallStoneTorchSettings(Function0<Block> groundStoneTorch) {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static AbstractBlock.Settings createChainLinkFenceSettings() {
        return PlatformCore.expected();
    }

    @ExpectPlatform
    @NotNull
    public static AbstractBlock.Settings createStoneLadderSettings() {
        return PlatformCore.expected();
    }
}
