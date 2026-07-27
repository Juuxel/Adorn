package juuxel.adorn.block.entity;

import com.google.common.base.Predicates;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.util.FluidTankReference;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.WorldlyContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class BrewerBlockEntityNeo extends BrewerBlockEntity implements BlockEntityWithFluidTank {
    private static final int CAPACITY = FLUID_CAPACITY_IN_BUCKETS * FluidType.BUCKET_VOLUME;

    private final FluidStacksResourceHandler tank = new FluidStacksResourceHandler(1, CAPACITY) {
        @Override
        protected void onContentsChanged(int index, FluidStack previousContents) {
            setChanged();
        }
    };
    private final FluidReference fluidReference = new FluidTankReference(tank, 0);

    public BrewerBlockEntityNeo(BlockPos pos, BlockState state) {
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

    private boolean moveFromFluidContainer(boolean commit) {
        try (var tx = Transaction.open(null)) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(new WorldlyContainerWrapper(this, null), FLUID_CONTAINER_SLOT);
            ResourceHandler<FluidResource> itemFluidHandler =
                getItem(FLUID_CONTAINER_SLOT).getCapability(Capabilities.Fluid.ITEM, itemAccess);

            if (itemFluidHandler != null) {
                int maxAmount = CAPACITY - tank.getAmountAsInt(0);
                int moved = ResourceHandlerUtil.move(itemFluidHandler, tank, Predicates.alwaysTrue(), maxAmount, tx);
                if (moved > 0) {
                    if (commit) tx.commit();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected boolean canExtractFluidContainer() {
        return moveFromFluidContainer(false);
    }

    @Override
    protected void tryExtractFluidContainer() {
        moveFromFluidContainer(true);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        tank.serialize(view);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        tank.deserialize(view);
    }
}
