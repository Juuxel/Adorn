package juuxel.adorn.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class InventoryWrappingRecipeInput<I extends Container> implements RecipeInput {
    protected final I parent;

    public InventoryWrappingRecipeInput(I parent) {
        this.parent = parent;
    }

    @Override
    public ItemStack getItem(int slot) {
        return parent.getItem(slot);
    }

    @Override
    public int size() {
        return parent.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return parent.isEmpty();
    }
}
