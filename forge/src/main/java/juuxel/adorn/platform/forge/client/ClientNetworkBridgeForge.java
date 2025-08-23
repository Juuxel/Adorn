package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.ClientNetworkBridge;
import net.minecraft.network.packet.CustomPayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public final class ClientNetworkBridgeForge implements ClientNetworkBridge {
    @Override
    public void sendToServer(CustomPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
    }
}
