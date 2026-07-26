package juuxel.adorn.platform.forge;

import juuxel.adorn.fluid.FluidAmountPredicate;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.fluid.FluidVolume;
import juuxel.adorn.platform.FluidBridge;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

public final class FluidBridgeForge implements FluidBridge {
    @Override
    public FluidUnit getFluidUnit() {
        return FluidUnit.LITRE;
    }

    @Nullable
    @Override
    public FluidVolume drain(Level world, BlockPos pos, @Nullable BlockState state, Direction side, Fluid fluid, FluidAmountPredicate amountPredicate) {
        // This method is a port of the Fabric fluid bridge code.
        var fluidHandler = world.getCapability(Capabilities.Fluid.BLOCK, pos, state, null, side);

        if (fluidHandler != null) {
            var upperBound = amountPredicate.getUpperBound();
            int maxAmount = (int) FluidUnit.convert(upperBound.getAmount(), upperBound.getUnit(), FluidUnit.LITRE);

            try (var tx = Transaction.open(null)) {
                var extracted = fluidHandler.extract(FluidResource.of(fluid), maxAmount, tx);

                if (extracted > 0 && amountPredicate.test(extracted, FluidUnit.LITRE)) {
                    tx.commit();
                    return new FluidVolume(fluid, extracted, DataComponentPatch.EMPTY, FluidUnit.LITRE);
                }
            }
        }

        return null;
    }
}
