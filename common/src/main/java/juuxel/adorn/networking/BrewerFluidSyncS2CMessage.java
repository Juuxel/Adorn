package juuxel.adorn.networking;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.fluid.FluidVolume;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record BrewerFluidSyncS2CMessage(int syncId, FluidVolume fluid) implements CustomPacketPayload {
    public static final Type<BrewerFluidSyncS2CMessage> ID = new Type<>(AdornCommon.id("brewer_fluid_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BrewerFluidSyncS2CMessage> PACKET_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        BrewerFluidSyncS2CMessage::syncId,
        FluidVolume.PACKET_CODEC,
        BrewerFluidSyncS2CMessage::fluid,
        BrewerFluidSyncS2CMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
