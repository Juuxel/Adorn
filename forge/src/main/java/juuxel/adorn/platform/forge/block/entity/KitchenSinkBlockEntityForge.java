package juuxel.adorn.platform.forge.block.entity;

import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.platform.forge.util.FluidTankReference;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.tags.FluidTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public final class KitchenSinkBlockEntityForge extends KitchenSinkBlockEntity implements BlockEntityWithFluidTank {
    // Bottles are 250 l in Adorn *on Forge*.
    private static final int BOTTLE_LITRES = 250;
    private static final int CAPACITY = FluidType.BUCKET_VOLUME;
    private static final FluidResource BOTTLE_WATER = FluidResource.of(Fluids.WATER);

    private final FluidStacksResourceHandler tank = new FluidStacksResourceHandler(1, CAPACITY) {
        @Override
        public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
            if (index == 0 && resource.equals(getResource(0)) && getLevel() instanceof ServerLevel world && supportsInfiniteExtraction(world, resource.getFluid())) {
                return Math.min(getAmountAsInt(0), amount);
            }

            return super.extract(index, resource, amount, transaction);
        }

        @Override
        protected void onContentsChanged(int index, FluidStack previousContents) {
            markDirtyAndSync();
        }
    };
    private final FluidReference fluidReference = new FluidTankReference(tank, 0);

    public KitchenSinkBlockEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public ResourceHandler<FluidResource> getTank() {
        return tank;
    }

    @Override
    public FluidReference getFluidReference() {
        return fluidReference;
    }

    @Override
    public boolean interactWithItem(ItemStack stack, Player player, InteractionHand hand) {
        if (FluidUtil.interactWithFluidHandler(player, hand, worldPosition, tank)) {
            markDirtyAndSync();
            return true;
        }

        var tankFluid = fluidReference.createSnapshot();

        // Special case bottles since they don't have a fluid handler.
        if (stack.is(Items.GLASS_BOTTLE)) {
            try (var tx = Transaction.open(null)) {
                var drainingResult = tank.extract(BOTTLE_WATER, BOTTLE_LITRES, tx);
                if (drainingResult >= BOTTLE_LITRES) {
                    tx.commit();
                    onPickUp(tankFluid, stack, player);
                    var bottle = PotionContents.createItemStack(Items.POTION, Potions.WATER);
                    setStackOrInsert(player, hand, bottle);
                    return true;
                }
            }
        } else if (stack.is(Items.POTION) && isWaterBottle(stack)) {
            try (var tx = Transaction.open(null)) {
                int inserted = tank.insert(BOTTLE_WATER, BOTTLE_LITRES, tx);
                if (inserted >= BOTTLE_LITRES) {
                    tx.commit();
                    onFill(stack, player);
                    setStackOrInsert(player, hand, new ItemStack(Items.GLASS_BOTTLE));
                    markDirtyAndSync();
                    return true;
                }
            }
        }

        return false;
    }

    private void setStackOrInsert(Player player, InteractionHand hand, ItemStack stack) {
        var current = player.getItemInHand(hand);
        current.shrink(1);

        if (current.isEmpty()) {
            player.setItemInHand(hand, stack);
        } else {
            player.getInventory().placeItemBackInInventory(stack);
        }
    }

    @Override
    public boolean clearFluidsWithSponge() {
        if (!tank.getResource(0).getFluid().is(FluidTags.WATER) || tank.getAmountAsInt(0) == 0) return false;

        tank.set(0, FluidResource.EMPTY, 0);
        markDirtyAndSync();
        return true;
    }

    @Override
    protected FluidItemSound getFillSound(FluidReference fluid, ItemStack stack) {
        return super.getFillSound(fluid, stack)
            .orElse(fluid.getFluid().getFluidType().getSound(FluidTankReference.toFluidStack(fluid), SoundActions.BUCKET_FILL));
    }

    @Override
    protected FluidItemSound getEmptySound(FluidReference fluid, ItemStack stack) {
        return super.getEmptySound(fluid, stack)
            .orElse(fluid.getFluid().getFluidType().getSound(FluidTankReference.toFluidStack(fluid), SoundActions.BUCKET_EMPTY));
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        tank.deserialize(view);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        tank.serialize(view);
    }

    @Override
    public int calculateComparatorOutput() {
        int amount = tank.getAmountAsInt(0);
        return amount == 0 ? 0 : 1 + Mth.floor(14 * (float) amount / (float) CAPACITY);
    }
}
