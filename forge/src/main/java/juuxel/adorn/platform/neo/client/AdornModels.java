package juuxel.adorn.platform.neo.client;

import juuxel.adorn.client.CustomModelKey;
import juuxel.adorn.client.CustomModelKeys;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

public final class AdornModels {
    @SubscribeEvent
    public static void addModels(ModelEvent.RegisterAdditional event) {
        for (CustomModelKey key : CustomModelKeys.MODEL_KEYS) {
            event.register(((CustomModelKeyImpl) key).modelId());
        }
    }
}
