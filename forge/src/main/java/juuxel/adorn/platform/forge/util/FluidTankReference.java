package juuxel.adorn.platform.forge.util;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import net.minecraft.component.ComponentChanges;
import net.minecraft.fluid.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public final class FluidTankReference extends FluidReference {
    private final StacksResourceHandler<FluidStack, FluidResource> tank;
    private final int slot;

    public FluidTankReference(StacksResourceHandler<FluidStack, FluidResource> tank, int slot) {
        this.tank = tank;
        this.slot = slot;
    }

    public ResourceHandler<FluidResource> getTank() {
        return tank;
    }

    @Override
    public Fluid getFluid() {
        return tank.getResource(slot).getFluid();
    }

    @Override
    public void setFluid(Fluid fluid, long amount, ComponentChanges components) {
        tank.set(slot, FluidResource.of(fluid, components), (int) amount);
    }

    @Override
    public long getAmount() {
        return tank.getAmountAsLong(slot);
    }

    @Override
    public void setAmount(long amount) {
        tank.set(slot, tank.getResource(slot), (int) amount);
    }

    @Override
    public ComponentChanges getComponents() {
        return tank.getResource(slot).getComponentsPatch();
    }

    @Override
    public FluidUnit getUnit() {
        return FluidUnit.LITRE;
    }

    /**
     * Converts this fluid reference to a {@link FluidStack}.
     * This is faster than a manual conversion for a {@code FluidTankReference}.
     */
    public static FluidStack toFluidStack(FluidReference reference) {
        if (reference instanceof FluidTankReference ftr) {
            return ftr.tank.getResource(ftr.slot).toStack(ftr.tank.getAmountAsInt(ftr.slot));
        } else {
            return new FluidStack(reference.getFluid(), (int) FluidUnit.convert(reference.getAmount(), reference.getUnit(), FluidUnit.LITRE), reference.getComponents());
        }
    }
}
