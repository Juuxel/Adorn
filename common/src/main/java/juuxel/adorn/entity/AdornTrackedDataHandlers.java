package juuxel.adorn.entity;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.core.Holder;

public final class AdornTrackedDataHandlers {
    public static final Registrar<EntityDataSerializer<?>> TRACKED_DATA_HANDLERS = RegistrarFactory.get().createForTrackedDataHandlers();

    public static final Registered<EntityDataSerializer<Holder<ConeVariant>>> CONE_VARIANT = register("cone_variant", ConeVariant.ENTRY_PACKET_CODEC);

    public static void init() {
    }

    private static <T> Registered<EntityDataSerializer<T>> register(String id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        return TRACKED_DATA_HANDLERS.register(id, () -> EntityDataSerializer.forValueType(codec));
    }
}
