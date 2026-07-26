package juuxel.adorn.networking;

import juuxel.adorn.AdornCommon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record SetTradeStackC2SMessage(int syncId, int slotId, ItemStack stack) implements CustomPacketPayload {
    public static final Type<SetTradeStackC2SMessage> ID = new Type<>(AdornCommon.id("set_trade_stack"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetTradeStackC2SMessage> PACKET_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        SetTradeStackC2SMessage::syncId,
        ByteBufCodecs.VAR_INT,
        SetTradeStackC2SMessage::slotId,
        ItemStack.STREAM_CODEC,
        SetTradeStackC2SMessage::stack,
        SetTradeStackC2SMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
