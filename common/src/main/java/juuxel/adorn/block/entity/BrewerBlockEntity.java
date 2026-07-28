package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.block.BrewerBlock;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.menu.BrewerMenu;
import juuxel.adorn.platform.ItemBridge;
import juuxel.adorn.recipe.AdornRecipeTypes;
import juuxel.adorn.recipe.BrewerInput;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.InventoryWrappingRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public abstract class BrewerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, BrewerMenu.BrewerInputProvider {
    private static final String NBT_PROGRESS = "Progress";
    private static final String NBT_CURRENT_RECIPE = "CurrentRecipe";
    public static final int CONTAINER_SIZE = 4;
    public static final int INPUT_SLOT = 0;
    public static final int LEFT_INGREDIENT_SLOT = 1;
    public static final int RIGHT_INGREDIENT_SLOT = 2;
    public static final int FLUID_CONTAINER_SLOT = 3;
    public static final int MAX_PROGRESS = 200;
    public static final int FLUID_CAPACITY_IN_BUCKETS = 2;

    private int progress = 0;
    private @Nullable ResourceKey<Recipe<?>> currentRecipe;
    private final ContainerData propertyDelegate = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                default -> throw new IllegalArgumentException("Unknown property: " + index);
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                default -> throw new IllegalArgumentException("Unknown property: " + index);
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public BrewerBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.BREWER.get(), pos, state, CONTAINER_SIZE);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inv) {
        return new BrewerMenu(syncId, inv, this, propertyDelegate, getFluidReference());
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        view.putInt(NBT_PROGRESS, progress);
        view.store(NBT_CURRENT_RECIPE, ExtraCodecs.optionalEmptyMap(ResourceKey.codec(Registries.RECIPE)), Optional.ofNullable(currentRecipe));
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        progress = view.getIntOr(NBT_PROGRESS, 0);
        currentRecipe = view.read(NBT_CURRENT_RECIPE, ExtraCodecs.optionalEmptyMap(ResourceKey.codec(Registries.RECIPE))).flatMap(x -> x).orElse(null);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        var facing = getBlockState().getValue(BrewerBlock.FACING);

        if (side == facing.getClockWise()) {
            return new int[] { LEFT_INGREDIENT_SLOT };
        } else if (side == facing.getCounterClockWise()) {
            return new int[] { RIGHT_INGREDIENT_SLOT };
        } else if (side == facing.getOpposite()) {
            return new int[] { FLUID_CONTAINER_SLOT };
        } else if (side == Direction.UP) {
            return new int[] { INPUT_SLOT };
        } else if (side == Direction.DOWN) {
            return new int[] { INPUT_SLOT, FLUID_CONTAINER_SLOT };
        } else {
            return new int[0];
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == INPUT_SLOT && !(stack.is(AdornTags.BREWING_INPUTS) && getItem(slot).isEmpty())) return false;
        if (slot == FLUID_CONTAINER_SLOT && !getItem(slot).isEmpty()) return false;
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return dir != Direction.DOWN && canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && (slot != FLUID_CONTAINER_SLOT || canExtractFluidContainer());
    }

    public int calculateComparatorOutput() {
        // If brewing has finished
        var mugStack = getItem(INPUT_SLOT);
        if (!mugStack.isEmpty() && !mugStack.is(AdornTags.BREWING_INPUTS)) {
            return 15;
        }

        var progressFraction = (float) progress / MAX_PROGRESS;
        var level = progressFraction * 14;
        return Mth.ceil(level);
    }

    public abstract FluidReference getFluidReference();

    /** {@return true if you can extract the fluid container as an item} */
    protected abstract boolean canExtractFluidContainer();

    /** Extract the fluid from the container, if possible. */
    protected abstract void tryExtractFluidContainer();

    private boolean isActive() {
        return progress != 0;
    }

    @Override
    public void fillStackedContents(StackedItemContents finder) {
        finder.accountStack(getItem(INPUT_SLOT));
        finder.accountStack(getItem(LEFT_INGREDIENT_SLOT));
        finder.accountStack(getItem(RIGHT_INGREDIENT_SLOT));
    }

    private static void decrementIngredient(BrewerBlockEntity brewer, int slot) {
        var stack = brewer.getItem(slot);
        var remainderTemplate = ItemBridge.get().getRecipeRemainder(stack);
        var remainder = remainderTemplate != null ? remainderTemplate.create() : ItemStack.EMPTY;
        stack.shrink(1);

        if (!remainder.isEmpty()) {
            if (stack.isEmpty()) {
                brewer.setItem(slot, remainder);
            } else {
                Containers.dropItemStack(brewer.level, brewer.worldPosition.getX() + 0.5, brewer.worldPosition.getY() + 0.5, brewer.worldPosition.getZ() + 0.5, remainder);
            }
        }
    }

    public static void tick(ServerLevel world, BlockPos pos, BlockState state, BrewerBlockEntity brewer) {
        var originallyActive = brewer.isActive();
        brewer.tryExtractFluidContainer();

        var dirty = false;
        var hasMug = !brewer.getItem(INPUT_SLOT).isEmpty();

        if (hasMug != state.getValue(BrewerBlock.HAS_MUG)) {
            world.setBlockAndUpdate(pos, state.setValue(BrewerBlock.HAS_MUG, hasMug));
        }

        var input = brewer.createRecipeInput();
        var recipeEntry = world.recipeAccess().getRecipeFor(AdornRecipeTypes.BREWING.get(), input, world);
        var key = recipeEntry.map(RecipeHolder::id).orElse(null);

        if (!Objects.equals(key, brewer.currentRecipe)) {
            brewer.currentRecipe = key;
            brewer.progress = 0;
            dirty = true;
        }

        var recipe = recipeEntry.map(RecipeHolder::value).orElse(null);
        if (recipe != null) {
            if (brewer.progress++ >= MAX_PROGRESS) {
                decrementIngredient(brewer, LEFT_INGREDIENT_SLOT);
                decrementIngredient(brewer, RIGHT_INGREDIENT_SLOT);
                brewer.setItem(INPUT_SLOT, recipe.assemble(input));

                if (recipe instanceof FluidBrewingRecipe fluidRecipe) {
                    brewer.getFluidReference().decrement(fluidRecipe.fluid().amount(), fluidRecipe.fluid().unit());
                }
            }

            dirty = true;
        }

        var activeNow = brewer.isActive();
        if (originallyActive != activeNow) {
            dirty = true;
            var newState = state.setValue(BrewerBlock.ACTIVE, activeNow);
            world.setBlockAndUpdate(pos, newState);
        }

        if (dirty) {
            setChanged(world, pos, state);
        }
    }

    @Override
    public BrewerInput createRecipeInput() {
        return new RecipeInputImpl(this);
    }

    private static final class RecipeInputImpl extends InventoryWrappingRecipeInput<BrewerBlockEntity> implements BrewerInput {
        private RecipeInputImpl(BrewerBlockEntity brewer) {
            super(brewer);
        }

        @Override
        public FluidReference getFluidReference() {
            return parent.getFluidReference();
        }
    }
}
