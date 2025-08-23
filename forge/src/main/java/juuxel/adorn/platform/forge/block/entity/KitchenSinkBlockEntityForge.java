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
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public final class KitchenSinkBlockEntityForge extends KitchenSinkBlockEntity implements BlockEntityWithFluidTank {
    // Bottles are 250 l in Adorn *on Forge*.
    private static final int BOTTLE_LITRES = 250;
    private static final FluidStack BOTTLE_WATER = new FluidStack(Fluids.WATER, BOTTLE_LITRES);

    private final FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME) {
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            if (getWorld() instanceof ServerWorld world && supportsInfiniteExtraction(world, fluid.getFluid())) {
                return fluid.copyWithAmount(Math.min(getFluidAmount(), maxDrain));
            }

            return super.drain(maxDrain, action);
        }

        @Override
        protected void onContentsChanged() {
            markDirtyAndSync();
        }
    };
    private final FluidReference fluidReference = new FluidTankReference(tank);

    public KitchenSinkBlockEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public FluidTank getTank() {
        return tank;
    }

    @Override
    public FluidReference getFluidReference() {
        return fluidReference;
    }

    @Override
    public boolean interactWithItem(ItemStack stack, PlayerEntity player, Hand hand) {
        if (tank.getSpace() > 0) {
            // The player in the tryEmpty/FillContainer calls is only used for sound.
            var result = FluidUtil.tryEmptyContainer(stack, tank, tank.getSpace(), null, true);

            if (result.isSuccess()) {
                onFill(stack, player);
                setStackOrInsert(player, hand, result.result);
                markDirtyAndSync();
                return true;
            }
        }

        // Store before filling the item from the tank
        var tankFluid = fluidReference.createSnapshot();
        var result = FluidUtil.tryFillContainer(stack, tank, tank.getFluidAmount(), null, true);

        if (result.isSuccess()) {
            onPickUp(tankFluid, stack, player);
            setStackOrInsert(player, hand, result.result);
            markDirtyAndSync();
            return true;
        }

        // Special case bottles since they don't have a fluid handler.
        if (stack.isOf(Items.GLASS_BOTTLE)) {
            var drainingResult = tank.drain(BOTTLE_WATER, IFluidHandler.FluidAction.SIMULATE);
            if (drainingResult.getAmount() >= BOTTLE_LITRES) {
                // Execute the draining for real this time.
                tank.drain(BOTTLE_WATER, IFluidHandler.FluidAction.EXECUTE);
                onPickUp(tankFluid, stack, player);
                var bottle = PotionContentsComponent.createStack(Items.POTION, Potions.WATER);
                setStackOrInsert(player, hand, bottle);
                return true;
            }
        } else if (stack.isOf(Items.POTION)) {
            var spaceForWater = tank.isEmpty() || (FluidStack.isSameFluidSameComponents(tank.getFluid(), BOTTLE_WATER) && tank.getSpace() >= BOTTLE_LITRES);

            if (spaceForWater && isWaterBottle(stack)) {
                onFill(stack, player);
                tank.fill(BOTTLE_WATER.copy(), IFluidHandler.FluidAction.EXECUTE);
                setStackOrInsert(player, hand, new ItemStack(Items.GLASS_BOTTLE));
                markDirtyAndSync();
                return true;
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
        if (!tank.getFluid().getFluid().isIn(FluidTags.WATER) || tank.isEmpty()) return false;

        tank.getFluid().setAmount(0);
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
        return tank.isEmpty() ? 0 : 1 + MathHelper.floor(14 * (float) tank.getFluidAmount() / (float) tank.getCapacity());
    }
}
