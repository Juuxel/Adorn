package juuxel.adorn.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

/**
 * A fluid ingredient for crafting.
 */
public record FluidIngredient(FluidKey fluid, long amount, DataComponentPatch components, FluidUnit unit) implements HasFluidAmount {
    public static final Codec<FluidIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        FluidKey.CODEC.fieldOf("fluid").forGetter(FluidIngredient::fluid),
        Codec.LONG.fieldOf("amount").forGetter(FluidIngredient::amount),
        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidIngredient::components),
        FluidUnit.CODEC.optionalFieldOf("unit", FluidUnit.LITRE).forGetter(FluidIngredient::unit)
    ).apply(instance, FluidIngredient::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidIngredient> PACKET_CODEC =
        StreamCodec.ofMember(FluidIngredient::write, FluidIngredient::load);

    public FluidIngredient(FluidKey fluid, long amount, FluidUnit unit) {
        this(fluid, amount, DataComponentPatch.EMPTY, unit);
    }

    private static FluidIngredient load(RegistryFriendlyByteBuf buf) {
        var fluid = FluidKey.load(buf);
        var amount = buf.readVarLong();
        var components = DataComponentPatch.STREAM_CODEC.decode(buf);
        var unit = buf.readEnum(FluidUnit.class);
        return new FluidIngredient(fluid, amount, components, unit);
    }

    private void write(RegistryFriendlyByteBuf buf) {
        fluid.write(buf);
        buf.writeVarLong(amount);
        DataComponentPatch.STREAM_CODEC.encode(buf, components);
        buf.writeEnum(unit);
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public FluidUnit getUnit() {
        return unit;
    }
}
