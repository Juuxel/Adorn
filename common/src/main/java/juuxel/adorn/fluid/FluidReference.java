package juuxel.adorn.fluid;

import juuxel.adorn.config.ConfigManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

import java.util.Objects;

/**
 * A mutable reference to a fluid volume.
 * This can be a {@link FluidVolume} or a block entity's
 * internal fluid volume.
 */
public abstract class FluidReference implements HasFluidAmount {
    public abstract Fluid getFluid();
    public abstract void setFluid(Fluid fluid, long amount, DataComponentPatch components);

    public abstract void setAmount(long amount);

    public abstract DataComponentPatch getComponents();

    public boolean isEmpty() {
        return getFluid() == Fluids.EMPTY || getAmount() == 0;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeEnum(getUnit());

        if (isEmpty()) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            buf.writeVarInt(BuiltInRegistries.FLUID.getId(getFluid()));
            buf.writeVarLong(getAmount());
            DataComponentPatch.STREAM_CODEC.encode(buf, getComponents());
        }
    }

    protected void readWithoutUnit(RegistryFriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            Fluid fluid = BuiltInRegistries.FLUID.byId(buf.readVarInt());
            long amount = buf.readVarLong();
            DataComponentPatch components = DataComponentPatch.STREAM_CODEC.decode(buf);
            setFluid(fluid, amount, components);
        } else {
            setFluid(Fluids.EMPTY, 0, DataComponentPatch.EMPTY);
        }
    }

    /**
     * Creates an independent mutable snapshot of this fluid reference's current contents.
     */
    public FluidVolume createSnapshot() {
        return new FluidVolume(getFluid(), getAmount(), getComponents(), getUnit());
    }

    public void increment(long amount, FluidUnit unit) {
        setAmount(getAmount() + FluidUnit.convert(amount, unit, getUnit()));
    }

    public void decrement(long amount, FluidUnit unit) {
        increment(-amount, unit);
    }

    public boolean matches(FluidIngredient ingredient) {
        return ingredient.fluid().matches(getFluid())
            && FluidUnit.compareVolumes(this, ingredient) >= 0
            && Objects.equals(getComponents(), ingredient.components());
    }

    public Component getAmountText() {
        var displayUnit = getDefaultDisplayUnit();
        return Component.translatable(
            "gui.adorn.fluid_volume",
            FluidUnit.losslessConvert(getAmount(), getUnit(), displayUnit).resizeFraction(getUnitDenominator(getUnit(), displayUnit)),
            displayUnit.getSymbol()
        );
    }

    public Component getAmountText(long max, FluidUnit maxUnit) {
        var displayUnit = getDefaultDisplayUnit();
        return Component.translatable(
            "gui.adorn.fluid_volume.fraction",
            FluidUnit.losslessConvert(getAmount(), getUnit(), displayUnit).resizeFraction(getUnitDenominator(getUnit(), displayUnit)).toString(),
            FluidUnit.losslessConvert(max, maxUnit, displayUnit).resizeFraction(getUnitDenominator(maxUnit, displayUnit)).toString(),
            displayUnit.getSymbol()
        );
    }

    private static FluidUnit getDefaultDisplayUnit() {
        return ConfigManager.config().client.displayedFluidUnit;
    }

    private static long getUnitDenominator(FluidUnit from, FluidUnit to) {
        if (from.getBucketVolume() == to.getBucketVolume()) return 1;
        return Math.max(1, from.getBucketVolume() / to.getBucketVolume());
    }

    @Override
    public String toString() {
        return "FluidReference(fluid=%s, amount=%d, nbt=%s)"
            .formatted(BuiltInRegistries.FLUID.getKey(getFluid()), getAmount(), getComponents());
    }

    public static boolean areFluidsEqual(FluidReference a, FluidReference b) {
        if (a.isEmpty()) return b.isEmpty();
        return a.getFluid() == b.getFluid() && Objects.equals(a.getComponents(), b.getComponents());
    }

    public static boolean areFluidsAndAmountsEqual(FluidReference a, FluidReference b) {
        if (a.isEmpty()) return b.isEmpty();
        return areFluidsEqual(a, b) && FluidUnit.compareVolumes(a, b) == 0;
    }
}
