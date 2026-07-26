package juuxel.adorn.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public sealed interface EntryOrTag<T> {
    @SuppressWarnings("unchecked")
    static <T> Codec<EntryOrTag<T>> codec(ResourceKey<Registry<T>> registryKey) {
        return Codec.either(
            TagKey.hashedCodec(registryKey),
            (Codec<T>) BuiltInRegistries.REGISTRY.getValue(registryKey.identifier()).byNameCodec()
        ).xmap(either -> either.map(EntryOrTag.OfTag::new, EntryOrTag.OfEntry::new), entryOrTag -> switch (entryOrTag) {
            case EntryOrTag.OfEntry(T value) -> Either.right(value);
            case EntryOrTag.OfTag(
                TagKey<T> inner) -> Either.left(inner);
        });
    }

    record OfEntry<T>(T value) implements EntryOrTag<T> {
    }

    record OfTag<T>(TagKey<T> tag) implements EntryOrTag<T> {
    }
}
