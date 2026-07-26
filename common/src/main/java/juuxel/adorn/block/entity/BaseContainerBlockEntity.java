package juuxel.adorn.block.entity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;

/**
 * A container block entity that might not have a menu.
 * This class handles the serialisation and the container logic.
 */
public abstract class BaseContainerBlockEntity extends RandomizableContainerBlockEntity {
    private final int size;
    private NonNullList<ItemStack> items;

    public BaseContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);
        items = NonNullList.withSize(size, ItemStack.EMPTY);
        this.size = size;
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        if (!trySaveLootTable(view)) {
            ContainerHelper.saveAllItems(view, items);
        }
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        items = NonNullList.withSize(size, ItemStack.EMPTY);
        if (!tryLoadLootTable(view)) {
            ContainerHelper.loadAllItems(view, items);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        items = list;
    }

    @Override
    public int getContainerSize() {
        return size;
    }

    @Override
    protected Component getDefaultName() {
        return getBlockState().getBlock().getName();
    }
}
