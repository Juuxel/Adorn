package juuxel.adorn.menu;

import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.fluid.FluidVolume;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.networking.BrewerFluidSyncS2CMessage;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.recipe.BrewerInput;
import juuxel.adorn.recipe.BrewingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.menu.AbstractRecipeMenu;
import net.minecraft.menu.property.ArrayPropertyDelegate;
import net.minecraft.menu.property.PropertyDelegate;
import net.minecraft.menu.slot.Slot;
import net.minecraft.recipe.InputSlotFiller;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.book.RecipeBookType;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class BrewerMenu extends AbstractRecipeMenu {
    private final Inventory container;
    private final PropertyDelegate propertyDelegate;
    private FluidReference fluid;
    private final PlayerEntity player;
    private @Nullable FluidVolume lastFluid = null;

    public BrewerMenu(int syncId, PlayerInventory playerInventory, Inventory container, PropertyDelegate propertyDelegate, FluidReference fluid) {
        super(AdornMenus.BREWER.get(), syncId);
        this.container = container;
        this.propertyDelegate = propertyDelegate;
        this.fluid = fluid;
        this.player = playerInventory.player;

        checkSize(container, BrewerBlockEntity.CONTAINER_SIZE);
        checkDataCount(propertyDelegate, 1);

        addSlot(new MainSlot(container, BrewerBlockEntity.INPUT_SLOT, 80, 56));
        addSlot(new Slot(container, BrewerBlockEntity.LEFT_INGREDIENT_SLOT, 50, 17));
        addSlot(new Slot(container, BrewerBlockEntity.RIGHT_INGREDIENT_SLOT, 110, 17));
        addSlot(new FluidContainerSlot(container, BrewerBlockEntity.FLUID_CONTAINER_SLOT, 123, 60));

        // Main player inventory
        for (int y = 0; y <= 2; y++) {
            for (int x = 0; x <= 8; x++) {
                addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        // Hotbar
        for (int x = 0; x <= 8; x++) {
            addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }

        addProperties(propertyDelegate);
    }
    
    public BrewerMenu(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(BrewerBlockEntity.CONTAINER_SIZE), new ArrayPropertyDelegate(1), FluidVolume.empty(FluidUnit.LITRE));
    }
    
    public int getProgress() {
        return propertyDelegate.get(0);
    }

    public FluidReference getFluid() {
        return fluid;
    }

    public void setFluid(FluidReference fluid) {
        this.fluid = fluid;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return container.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        var result = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot.hasStack()) {
            var stack = slot.getStack();
            result = stack.copy();

            if (index <= BrewerBlockEntity.FLUID_CONTAINER_SLOT) {
                if (!insertItem(stack, BrewerBlockEntity.FLUID_CONTAINER_SLOT + 1, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                Slot mugSlot = slots.get(BrewerBlockEntity.INPUT_SLOT);

                if (!mugSlot.hasStack() && mugSlot.canInsert(stack)) {
                    mugSlot.setStack(stack.split(Math.min(mugSlot.getMaxItemCount(stack), stack.getCount())));
                }

                if (!stack.isEmpty() && !insertItem(stack, BrewerBlockEntity.LEFT_INGREDIENT_SLOT, BrewerBlockEntity.FLUID_CONTAINER_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return result;
    }

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();

        var last = lastFluid;
        if (last == null || !FluidReference.areFluidsAndAmountsEqual(fluid, last)) {
            lastFluid = fluid.createSnapshot();
            PlatformBridges.get().getNetwork().sendToClient(player, new BrewerFluidSyncS2CMessage(syncId, fluid.createSnapshot()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PostFillAction fillInputSlots(boolean craftAll, boolean creative, RecipeEntry<?> recipe, ServerWorld world, PlayerInventory inventory) {
        List<Slot> inputSlots = slots.subList(0, 3);
        BrewerInput input = ((BrewerInputProvider) container).createRecipeInput();
        // Note: in the call below, width * height must equal the number of input slots in the recipe (3).
        // Thus, we pretend that we have a row of three slots.
        return InputSlotFiller.fill(new InputSlotFiller.Handler<>() {
            @Override
            public void populateRecipeFinder(RecipeFinder finder) {
                BrewerMenu.this.populateRecipeFinder(finder);
            }

            @Override
            public void clear() {
                inputSlots.forEach(slot -> slot.setStackNoCallbacks(ItemStack.EMPTY));
            }

            @Override
            public boolean matches(RecipeEntry<BrewingRecipe> entry) {
                return entry.value().matches(input, world);
            }
        }, 3, 1, inputSlots, inputSlots, player.getInventory(), (RecipeEntry<BrewingRecipe>) recipe, craftAll, creative);
    }

    @Override
    public void populateRecipeFinder(RecipeFinder finder) {
        if (container instanceof RecipeInputProvider inputProvider) {
            inputProvider.provideRecipeInputs(finder);
        }
    }

    @Override
    public RecipeBookType getCategory() {
        // seems to only be used for toggling the gui elements
        return RecipeBookType.CRAFTING;
    }

    public Slot getMainSlot() {
        return slots.getFirst();
    }

    public Slot getFirstIngredientSlot() {
        return slots.get(1);
    }

    public Slot getSecondIngredientSlot() {
        return slots.get(2);
    }

    private static final class MainSlot extends Slot {
        private MainSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.isIn(AdornTags.BREWING_INPUTS);
        }
    }

    private static final class FluidContainerSlot extends Slot {
        private FluidContainerSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }
    }

    public interface BrewerInputProvider {
        BrewerInput createRecipeInput();
    }
}
