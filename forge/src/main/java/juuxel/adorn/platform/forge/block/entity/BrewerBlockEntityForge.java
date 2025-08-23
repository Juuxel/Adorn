package juuxel.adorn.platform.forge.block.entity;

import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.platform.forge.util.FluidTankReference;
import net.minecraft.block.BlockState;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public final class BrewerBlockEntityForge extends BrewerBlockEntity implements BlockEntityWithFluidTank {
    private final FluidTank tank = new FluidTank(FLUID_CAPACITY_IN_BUCKETS * FluidType.BUCKET_VOLUME) {
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };
    private final FluidReference fluidReference = new FluidTankReference(tank);

    public BrewerBlockEntityForge(BlockPos pos, BlockState state) {
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
    protected boolean canExtractFluidContainer() {
        return !FluidUtil.tryEmptyContainer(getStack(FLUID_CONTAINER_SLOT), tank, tank.getSpace(), null, false).isSuccess();
    }

    @Override
    protected void tryExtractFluidContainer() {
        var result = FluidUtil.tryEmptyContainer(getStack(FLUID_CONTAINER_SLOT), tank, tank.getSpace(), null, true);

        if (result.isSuccess()) {
            setStack(FLUID_CONTAINER_SLOT, result.result);
            markDirty();
        }
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        tank.serialize(view);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        tank.deserialize(view);
    }
}
