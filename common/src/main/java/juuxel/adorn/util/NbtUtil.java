package juuxel.adorn.util;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.jetbrains.annotations.Nullable;

public final class NbtUtil {
    // Registry-aware NBT compound methods

    public static <T> @Nullable T getWithCodec(NbtCompound nbt, String name, Codec<T> codec, RegistryWrapper.WrapperLookup registries) {
        var ops = registries.getOps(NbtOps.INSTANCE);
        return nbt.get(name, codec, ops).orElse(null);
    }

    public static <T> T getWithCodec(NbtCompound nbt, String name, Codec<T> codec, RegistryWrapper.WrapperLookup registries, T fallback) {
        var decoded = getWithCodec(nbt, name, codec, registries);
        return decoded != null ? decoded : fallback;
    }

    public static <T> void putWithCodec(NbtCompound nbt, String name, Codec<T> codec, T value, RegistryWrapper.WrapperLookup registries) {
        var ops = registries.getOps(NbtOps.INSTANCE);
        nbt.put(name, codec, ops, value);
    }

    // Specific data types

    public static void putText(NbtCompound nbt, String name, Text text, RegistryWrapper.WrapperLookup registries) {
        putWithCodec(nbt, name, TextCodecs.CODEC, text, registries);
    }

    public static @Nullable Text getText(NbtCompound nbt, String name, RegistryWrapper.WrapperLookup registries) {
        return getWithCodec(nbt, name, TextCodecs.CODEC, registries);
    }
}
