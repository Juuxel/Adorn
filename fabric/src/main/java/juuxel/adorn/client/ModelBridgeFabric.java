package juuxel.adorn.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;

public final class ModelBridgeFabric implements ModelBridge {
    @Override
    public <T> CustomModelKey<T> createModelKey(Identifier id, CustomModelKey.Type<T> type) {
        return new CustomModelKeyImpl<>(id, type, ExtraModelKey.create(id::toString));
    }

    @Override
    public <T> T getModel(ModelManager manager, CustomModelKey<T> key) {
        return manager.getModel(((CustomModelKeyImpl<T>) key).backing);
    }

    record CustomModelKeyImpl<T>(
        Identifier id, CustomModelKey.Type<T> type, ExtraModelKey<T> backing) implements CustomModelKey<T> {
    }
}
