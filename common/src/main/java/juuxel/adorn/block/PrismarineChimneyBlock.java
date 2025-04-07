package juuxel.adorn.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class PrismarineChimneyBlock extends AbstractChimneyBlock implements BlockWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.prismarine_chimney.description";

    public PrismarineChimneyBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.getFluidState().isIn(FluidTags.WATER) || state.get(CONNECTED)) return;

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.9;
        double z = pos.getZ() + 0.5;

        for (int i = 0; i < 3; i++) {
            world.addImportantParticleClient(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    public static final class WithColumn extends PrismarineChimneyBlock {
        private final boolean drag;

        public WithColumn(boolean drag, Settings settings) {
            super(settings);
            this.drag = drag;
        }

        public boolean getDrag() {
            return drag;
        }

        @Override
        public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            BubbleColumnBlock.update(world, pos.up(), state);
        }

        @Override
        public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
            world.scheduleBlockTick(pos, this, 20);
        }

        @Override
        protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
            world.scheduleBlockTick(pos, this, 20);
        }

        @Override
        public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
            if (!drag) {
                super.randomDisplayTick(state, world, pos, random);
            }
        }
    }
}
