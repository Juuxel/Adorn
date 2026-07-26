package juuxel.adorn.fluid;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public final class FluidVolume extends FluidReference {
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidVolume> PACKET_CODEC = StreamCodec.ofMember(FluidVolume::write, FluidVolume::load);
    private Fluid fluid;
    private long amount;
    private DataComponentPatch components;
    private final FluidUnit unit;

    public FluidVolume(Fluid fluid, long amount, DataComponentPatch components, FluidUnit unit) {
        this.fluid = fluid;
        this.amount = amount;
        this.components = components;
        this.unit = unit;
    }

    public static FluidVolume empty(FluidUnit unit) {
        return new FluidVolume(Fluids.EMPTY, 0, DataComponentPatch.EMPTY, unit);
    }

    public static FluidVolume load(RegistryFriendlyByteBuf buf) {
        var volume = empty(buf.readEnum(FluidUnit.class));
        volume.readWithoutUnit(buf);
        return volume;
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public void setFluid(Fluid fluid, long amount, DataComponentPatch components) {
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
    public DataComponentPatch getComponents() {
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
