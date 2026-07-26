package juuxel.adorn.component;

import com.mojang.serialization.Codec;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.core.Holder;

import java.util.Optional;

public record ConeVariantComponent(
    EitherHolder<ConeVariant> variant) {
    public static final Codec<ConeVariantComponent> CODEC =
        EitherHolder.codec(AdornRegistryKeys.CONE_VARIANT, ConeVariant.REGISTRY_CODEC)
            .xmap(ConeVariantComponent::new, ConeVariantComponent::variant);

    public static final StreamCodec<RegistryFriendlyByteBuf, ConeVariantComponent> PACKET_CODEC =
        EitherHolder.streamCodec(AdornRegistryKeys.CONE_VARIANT, ConeVariant.ENTRY_PACKET_CODEC)
            .map(ConeVariantComponent::new, ConeVariantComponent::variant);

    public ConeVariantComponent(Holder<ConeVariant> variant) {
        this(new EitherHolder<>(variant));
    }

    public ConeVariantComponent(ResourceKey<ConeVariant> variant) {
        this(new EitherHolder<>(variant));
    }

    public Optional<Holder<ConeVariant>> getVariant(HolderLookup.Provider registries) {
        return variant.unwrap(registries);
    }
}
