package juuxel.adorn.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;

public final class AdornModels {
    @SuppressWarnings("unchecked")
    public static void init() {
        ModelLoadingPlugin.register(context -> {
            for (var key : CustomModelKeys.MODEL_KEYS) {
                var impl = (ModelBridgeFabric.CustomModelKeyImpl<?>) key;
                if (impl.type() == CustomModelKey.BLOCK_STATE_MODEL) {
                    context.addModel((ExtraModelKey<BlockStateModel>) impl.backing(), SimpleUnbakedExtraModel.blockStateModel(impl.id()));
                } else {
                    throw new UnsupportedOperationException("[Adorn] Unknown type: " + impl.type());
                }
            }
        });
    }
}
