package juuxel.adorn.entity;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.DyeColor;

public final class AdornTrackedDataHandlers {
    public static final Registrar<TrackedDataHandler<?>> TRACKED_DATA_HANDLERS = RegistrarFactory.get().createForTrackedDataHandlers();

    public static final Registered<TrackedDataHandler<DyeColor>> DYE_COLOR = register("dye_color", DyeColor.PACKET_CODEC);

    public static void init() {
    }

    private static <T> Registered<TrackedDataHandler<T>> register(String id, PacketCodec<? super RegistryByteBuf, T> codec) {
        return TRACKED_DATA_HANDLERS.register(id, () -> TrackedDataHandler.create(codec));
    }
}
