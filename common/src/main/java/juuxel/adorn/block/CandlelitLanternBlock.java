package juuxel.adorn.block;

import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public final class CandlelitLanternBlock extends LanternBlock implements BlockWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.candlelit_lantern.description";

    public CandlelitLanternBlock(Properties settings) {
        super(settings);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        var px = 1 / 16.0;
        var vec = Vec3.upFromBottomCenterOf(pos, state.getValue(HANGING) ? 6 * px : 5 * px);
        AbstractCandleBlock.addParticlesAndSound(world, vec, random);
    }

    public static Properties createBlockSettings() {
        return Properties.of()
            .mapColor(MapColor.METAL)
            .forceSolidOn()
            .requiresCorrectToolForDrops()
            .strength(3.5f)
            .sound(SoundType.LANTERN)
            .lightLevel(state -> 12)
            .noOcclusion();
    }
}
