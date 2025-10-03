package juuxel.adorn.fluid;

import net.minecraft.component.ComponentChanges;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public final class FluidVolume extends FluidReference {
    public static final PacketCodec<RegistryByteBuf, FluidVolume> PACKET_CODEC = PacketCodec.of(FluidVolume::write, FluidVolume::load);
    private Fluid fluid;
    private long amount;
    private ComponentChanges components;
    private final FluidUnit unit;

    public FluidVolume(Fluid fluid, long amount, ComponentChanges components, FluidUnit unit) {
        this.fluid = fluid;
        this.amount = amount;
        this.components = components;
        this.unit = unit;
    }

    public static FluidVolume empty(FluidUnit unit) {
        return new FluidVolume(Fluids.EMPTY, 0, ComponentChanges.EMPTY, unit);
    }

    public static FluidVolume load(RegistryByteBuf buf) {
        var volume = empty(buf.readEnumConstant(FluidUnit.class));
        volume.readWithoutUnit(buf);
        return volume;
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public void setFluid(Fluid fluid, long amount, ComponentChanges components) {
        this.fluid = fluid;
        this.amount = amount;
        this.components = components;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public ComponentChanges getComponents() {
        return components;
    }

    @Override
    public FluidUnit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "FluidVolume(fluid=%s, amount=%d, components=%s)".formatted(fluid, amount, components);
    }
}
