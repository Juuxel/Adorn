package juuxel.adorn.platform.forge.block.entity;

import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.platform.forge.util.FluidTankReference;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
            if (index == 0 && resource.equals(getResource(0)) && getWorld() instanceof ServerWorld world && supportsInfiniteExtraction(world, resource.getFluid())) {
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
    public boolean interactWithItem(ItemStack stack, PlayerEntity player, Hand hand) {
        if (FluidUtil.interactWithFluidHandler(player, hand, pos, tank)) {
            markDirtyAndSync();
            return true;
        }

        var tankFluid = fluidReference.createSnapshot();

        // Special case bottles since they don't have a fluid handler.
        if (stack.isOf(Items.GLASS_BOTTLE)) {
            try (var tx = Transaction.open(null)) {
                var drainingResult = tank.extract(BOTTLE_WATER, BOTTLE_LITRES, tx);
                if (drainingResult >= BOTTLE_LITRES) {
                    tx.commit();
                    onPickUp(tankFluid, stack, player);
                    var bottle = PotionContentsComponent.createStack(Items.POTION, Potions.WATER);
                    setStackOrInsert(player, hand, bottle);
                    return true;
                }
            }
        } else if (stack.isOf(Items.POTION) && isWaterBottle(stack)) {
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

    private void setStackOrInsert(PlayerEntity player, Hand hand, ItemStack stack) {
        var current = player.getStackInHand(hand);
        current.decrement(1);

        if (current.isEmpty()) {
            player.setStackInHand(hand, stack);
        } else {
            player.getInventory().offerOrDrop(stack);
        }
    }

    @Override
    public boolean clearFluidsWithSponge() {
        if (!tank.getResource(0).getFluid().isIn(FluidTags.WATER) || tank.getAmountAsInt(0) == 0) return false;

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
    protected void readData(ReadView view) {
        super.readData(view);
        tank.deserialize(view);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        tank.serialize(view);
    }

    @Override
    public int calculateComparatorOutput() {
        int amount = tank.getAmountAsInt(0);
        return amount == 0 ? 0 : 1 + MathHelper.floor(14 * (float) amount / (float) CAPACITY);
    }
}
