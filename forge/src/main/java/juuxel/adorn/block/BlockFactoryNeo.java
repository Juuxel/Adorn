package juuxel.adorn.block;

import juuxel.adorn.platform.BlockFactory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class BlockFactoryNeo implements BlockFactory {
    public static final BlockFactoryNeo INSTANCE = new BlockFactoryNeo();

    @Override
    public SofaBlock createSofa(BlockBehaviour.Properties settings) {
        return new SofaBlockNeo(settings);
    }

    @Override
    public Block createPaintedPlanks(BlockBehaviour.Properties settings) {
        return new PaintedPlanksBlockNeo(settings);
    }

    @Override
    public Block createPaintedWoodSlab(BlockBehaviour.Properties settings) {
        return new PaintedWoodSlabBlockNeo(settings);
    }

    @Override
    public Block createPaintedWoodStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new PaintedWoodStairsBlockNeo(baseBlockState, settings);
    }

    @Override
    public Block createPaintedWoodFence(BlockBehaviour.Properties settings) {
        return new PaintedWoodFenceBlockNeo(settings);
    }

    @Override
    public Block createPaintedWoodFenceGate(WoodType woodType, BlockBehaviour.Properties settings) {
        return new PaintedWoodFenceGateBlockNeo(woodType, settings);
    }
}
