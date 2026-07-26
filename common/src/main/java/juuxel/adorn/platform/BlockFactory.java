package juuxel.adorn.platform;

import juuxel.adorn.block.SofaBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public interface BlockFactory {
    BlockFactory DEFAULT = new BlockFactory() {};

    default SofaBlock createSofa(BlockBehaviour.Properties settings) {
        return new SofaBlock(settings);
    }

    default Block createPaintedPlanks(BlockBehaviour.Properties settings) {
        return new Block(settings);
    }

    default Block createPaintedWoodSlab(BlockBehaviour.Properties settings) {
        return new SlabBlock(settings);
    }

    default Block createPaintedWoodStairs(BlockState baseBlockState, BlockBehaviour.Properties settings) {
        return new StairBlock(baseBlockState, settings);
    }

    default Block createPaintedWoodFence(BlockBehaviour.Properties settings) {
        return new FenceBlock(settings);
    }

    default Block createPaintedWoodFenceGate(WoodType woodType, BlockBehaviour.Properties settings) {
        return new FenceGateBlock(woodType, settings);
    }
}
