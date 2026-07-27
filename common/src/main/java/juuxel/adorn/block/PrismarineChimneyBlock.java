package juuxel.adorn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

public class PrismarineChimneyBlock extends AbstractChimneyBlock implements BlockWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.prismarine_chimney.description";

    public PrismarineChimneyBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (!state.getFluidState().is(FluidTags.WATER) || state.getValue(CONNECTED)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.9;
        double z = pos.getZ() + 0.5;

        for (int i = 0; i < 3; i++) {
            world.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    public static final class WithColumn extends PrismarineChimneyBlock {
        private final boolean drag;

        public WithColumn(boolean drag, Properties settings) {
            super(settings);
            this.drag = drag;
        }

        public boolean getDrag() {
            return drag;
        }

        @Override
        public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
            BubbleColumnBlock.updateColumn(Blocks.BUBBLE_COLUMN, world, pos.above(), state);
        }

        @Override
        public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
            world.scheduleTick(pos, this, 20);
        }

        @Override
        protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
            world.scheduleTick(pos, this, 20);
        }

        @Override
        public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
            if (!drag) {
                super.animateTick(state, world, pos, random);
            }
        }
    }
}
