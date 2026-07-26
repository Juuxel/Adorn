package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.ClientNetworkBridge;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public final class ClientNetworkBridgeForge implements ClientNetworkBridge {
    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }
}
