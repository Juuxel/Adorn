package juuxel.adorn.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.Predicate;

public final class AdornBlockEntityType<E extends BlockEntity> extends BlockEntityType<E> {
    private final Predicate<Block> blockPredicate;

    public AdornBlockEntityType(BlockEntitySupplier<? extends E> factory, Predicate<Block> blockPredicate) {
        super(factory, Set.of());
        this.blockPredicate = blockPredicate;
    }

    @Override
    public boolean isValid(BlockState state) {
        return blockPredicate.test(state.getBlock());
    }
}
