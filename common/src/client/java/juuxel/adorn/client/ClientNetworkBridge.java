package juuxel.adorn.client;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@InlineServices
public interface ClientNetworkBridge {
    void sendToServer(CustomPacketPayload payload);

    @InlineServices.Getter
    static ClientNetworkBridge get() {
        return Services.load(ClientNetworkBridge.class);
    }
}
