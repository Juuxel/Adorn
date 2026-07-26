package juuxel.adorn.platform.forge;

import juuxel.adorn.block.SofaBlock;
import juuxel.adorn.platform.BlockFactory;
import juuxel.adorn.platform.forge.block.PaintedPlanksBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodFenceBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodFenceGateBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodSlabBlockNeo;
import juuxel.adorn.platform.forge.block.PaintedWoodStairsBlockNeo;
import juuxel.adorn.platform.forge.block.SofaBlockForge;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class BlockFactoryImpl implements BlockFactory {
    public static final BlockFactoryImpl INSTANCE = new BlockFactoryImpl();

    @Override
    public SofaBlock createSofa(BlockBehaviour.Properties settings) {
        return new SofaBlockForge(settings);
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
