package juuxel.adorn.networking;

import juuxel.adorn.AdornCommon;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record OpenBookS2CMessage(
    Identifier bookId) implements CustomPacketPayload {
    public static final Type<OpenBookS2CMessage> ID = new Type<>(AdornCommon.id("open_book"));
    public static final StreamCodec<FriendlyByteBuf, OpenBookS2CMessage> PACKET_CODEC =
        Identifier.STREAM_CODEC.map(OpenBookS2CMessage::new, OpenBookS2CMessage::bookId).cast();

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
