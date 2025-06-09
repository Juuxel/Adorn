package juuxel.adorn.component;

import com.mojang.serialization.Codec;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Optional;

public record ConeVariantComponent(LazyRegistryEntryReference<ConeVariant> variant) {
    public static final Codec<ConeVariantComponent> CODEC =
        LazyRegistryEntryReference.createCodec(AdornRegistryKeys.CONE_VARIANT, ConeVariant.REGISTRY_CODEC)
            .xmap(ConeVariantComponent::new, ConeVariantComponent::variant);

    public static final PacketCodec<RegistryByteBuf, ConeVariantComponent> PACKET_CODEC =
        LazyRegistryEntryReference.createPacketCodec(AdornRegistryKeys.CONE_VARIANT, ConeVariant.ENTRY_PACKET_CODEC)
            .xmap(ConeVariantComponent::new, ConeVariantComponent::variant);

    public ConeVariantComponent(RegistryEntry<ConeVariant> variant) {
        this(new LazyRegistryEntryReference<>(variant));
    }

    public ConeVariantComponent(RegistryKey<ConeVariant> variant) {
        this(new LazyRegistryEntryReference<>(variant));
    }

    public Optional<RegistryEntry<ConeVariant>> getVariant(RegistryWrapper.WrapperLookup registries) {
        return variant.resolveEntry(registries);
    }
}
