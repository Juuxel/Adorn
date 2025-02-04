package juuxel.adorn.platform.forge.util;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.fluid.FluidVolume;
import net.minecraft.component.ComponentChanges;
import net.minecraft.fluid.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public final class FluidTankReference extends FluidReference {
    private final FluidTank tank;

    public FluidTankReference(FluidTank tank) {
        this.tank = tank;
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public Fluid getFluid() {
        return tank.getFluid().getFluid();
    }

    @Override
    public void setFluid(Fluid fluid) {
        tank.setFluid(new FluidStack(fluid.getRegistryEntry(), tank.getFluid().getAmount(), tank.getFluid().getComponentsPatch()));
    }

    @Override
    public long getAmount() {
        return tank.getFluid().getAmount();
    }

    @Override
    public void setAmount(long amount) {
        tank.getFluid().setAmount((int) amount);
    }

    @Override
    public ComponentChanges getComponents() {
        return tank.getFluid().getComponentsPatch();
    }

    @Override
    public void setComponents(ComponentChanges changes) {
        tank.getFluid().getComponents().setChanges(changes);
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
            return ftr.tank.getFluid();
        } else {
            return new FluidStack(reference.getFluid().getRegistryEntry(), (int) FluidUnit.convert(reference.getAmount(), reference.getUnit(), FluidUnit.LITRE), reference.getComponents());
        }
    }

    public static FluidVolume toFluidVolume(FluidStack stack) {
        return new FluidVolume(stack.getFluid(), stack.getAmount(), stack.getComponentsPatch(), FluidUnit.LITRE);
    }
}
