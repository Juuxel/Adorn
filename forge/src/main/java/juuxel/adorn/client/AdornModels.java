package juuxel.adorn.client;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public final class AdornModels {
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void addModels(ModelEvent.RegisterStandalone event) {
        for (var key : CustomModelKeys.MODEL_KEYS) {
            var impl = (CustomModelKeyImpl<?>) key;
            if (impl.type() == CustomModelKey.BLOCK_STATE_MODEL) {
                event.register((StandaloneModelKey<BlockStateModel>) impl.backing(), SimpleUnbakedStandaloneModel.blockStateModel(key.id()));
            } else {
                throw new UnsupportedOperationException("[Adorn] Unknown type: " + impl.type());
            }
        }
    }
}
