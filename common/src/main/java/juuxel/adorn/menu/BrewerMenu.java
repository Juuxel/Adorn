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
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class BrewerMenu extends RecipeBookMenu {
    private final Container container;
    private final ContainerData propertyDelegate;
    private FluidReference fluid;
    private final Player player;
    private @Nullable FluidVolume lastFluid = null;

    public BrewerMenu(int syncId, Inventory playerInventory, Container container, ContainerData propertyDelegate, FluidReference fluid) {
        super(AdornMenus.BREWER.get(), syncId);
        this.container = container;
        this.propertyDelegate = propertyDelegate;
        this.fluid = fluid;
        this.player = playerInventory.player;

        checkContainerSize(container, BrewerBlockEntity.CONTAINER_SIZE);
        checkContainerDataCount(propertyDelegate, 1);

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

        addDataSlots(propertyDelegate);
    }
    
    public BrewerMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(BrewerBlockEntity.CONTAINER_SIZE), new SimpleContainerData(1), FluidVolume.empty(FluidUnit.LITRE));
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
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var result = ItemStack.EMPTY;
        var slot = slots.get(index);

        if (slot.hasItem()) {
            var stack = slot.getItem();
            result = stack.copy();

            if (index <= BrewerBlockEntity.FLUID_CONTAINER_SLOT) {
                if (!moveItemStackTo(stack, BrewerBlockEntity.FLUID_CONTAINER_SLOT + 1, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                Slot mugSlot = slots.get(BrewerBlockEntity.INPUT_SLOT);

                if (!mugSlot.hasItem() && mugSlot.mayPlace(stack)) {
                    mugSlot.setByPlayer(stack.split(Math.min(mugSlot.getMaxStackSize(stack), stack.getCount())));
                }

                if (!stack.isEmpty() && !moveItemStackTo(stack, BrewerBlockEntity.LEFT_INGREDIENT_SLOT, BrewerBlockEntity.FLUID_CONTAINER_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        var last = lastFluid;
        if (last == null || !FluidReference.areFluidsAndAmountsEqual(fluid, last)) {
            lastFluid = fluid.createSnapshot();
            PlatformBridges.get().getNetwork().sendToClient(player, new BrewerFluidSyncS2CMessage(containerId, fluid.createSnapshot()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public PostPlaceAction handlePlacement(boolean craftAll, boolean creative, RecipeHolder<?> recipe, ServerLevel world, Inventory inventory) {
        List<Slot> inputSlots = slots.subList(0, 3);
        BrewerInput input = ((BrewerInputProvider) container).createRecipeInput();
        // Note: in the call below, width * height must equal the number of input slots in the recipe (3).
        // Thus, we pretend that we have a row of three slots.
        return ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(StackedItemContents finder) {
                BrewerMenu.this.fillCraftSlotsStackedContents(finder);
            }

            @Override
            public void clearCraftingContent() {
                inputSlots.forEach(slot -> slot.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(RecipeHolder<BrewingRecipe> entry) {
                return entry.value().matches(input, world);
            }
        }, 3, 1, inputSlots, inputSlots, player.getInventory(), (RecipeHolder<BrewingRecipe>) recipe, craftAll, creative);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents finder) {
        if (container instanceof StackedContentsCompatible inputProvider) {
            inputProvider.fillStackedContents(finder);
        }
    }

    @Override
    public RecipeBookType getRecipeBookType() {
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
        private MainSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(AdornTags.BREWING_INPUTS);
        }
    }

    private static final class FluidContainerSlot extends Slot {
        private FluidContainerSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    public interface BrewerInputProvider {
        BrewerInput createRecipeInput();
    }
}
