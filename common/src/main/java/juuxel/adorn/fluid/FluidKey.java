package juuxel.adorn.fluid;

import com.mojang.serialization.Codec;
import juuxel.adorn.util.EntryOrTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/// A "key" that identifies a fluid or a group of fluids.
///
/// Could be a single fluid, a tag or a list of the former.
///
/// ## JSON format
///
/// A fluid key is one of:
///
/// - a string; if prefixed with `#`, a tag, otherwise a fluid ID
/// - an array of strings as described above
///
/// Examples: `"minecraft:water"`, `"#c:milk"`, `["minecraft:water", "minecraft:lava"]`
public sealed interface FluidKey permits FluidKeyImpl.Simple, FluidKeyImpl.OfArray {
    Codec<FluidKey> CODEC = FluidKeyImpl.CODEC;

    static FluidKey of(Fluid fluid) {
        return new FluidKeyImpl.Simple(new EntryOrTag.OfEntry<>(fluid));
    }

    static FluidKey of(TagKey<Fluid> tag) {
        return new FluidKeyImpl.Simple(new EntryOrTag.OfTag<>(tag));
    }

    /**
     * Returns the set of all fluids matching this key.
     */
    Set<Fluid> getFluids();

    /**
     * Tests whether the fluid matches this key.
     */
    boolean matches(Fluid fluid);

    /**
     * Writes this key to a packet buffer.
     * @see #load
     */
    default void write(FriendlyByteBuf buf) {
        var fluids = getFluids();
        buf.writeVarInt(fluids.size());

        for (Fluid fluid : fluids) {
            buf.writeVarInt(BuiltInRegistries.FLUID.getId(fluid));
        }
    }

    /**
     * Reads a key from a packet buffer.
     * @see #write
     */
    static FluidKey load(FriendlyByteBuf buf) {
        var size = buf.readVarInt();

        if (size == 1) {
            return new FluidKeyImpl.Simple(new EntryOrTag.OfEntry<>(BuiltInRegistries.FLUID.byId(buf.readVarInt())));
        } else {
            List<FluidKeyImpl.Simple> children = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                children.add(new FluidKeyImpl.Simple(new EntryOrTag.OfEntry<>(BuiltInRegistries.FLUID.byId(buf.readVarInt()))));
            }
            return new FluidKeyImpl.OfArray(children);
        }
    }
}
