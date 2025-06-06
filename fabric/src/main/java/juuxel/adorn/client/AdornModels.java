package juuxel.adorn.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public final class AdornModels {
    public static void init() {
        ModelLoadingPlugin.register(context -> {
            for (var key : CustomModelKeys.MODEL_KEYS) {
                var impl = (ModelBridgeFabric.CustomModelKeyImpl) key;
                context.addModels(impl.id());
            }
        });
    }
}
