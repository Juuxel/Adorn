package juuxel.adorn.platform.neo.client;

import juuxel.adorn.client.CustomModelKey;
import juuxel.adorn.client.ModelBridge;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

public final class ModelBridgeNeo implements ModelBridge {
    @Override
    public <T> CustomModelKey<T> createModelKey(Identifier id, CustomModelKey.Type<T> type) {
        return new CustomModelKeyImpl<>(type, new StandaloneModelKey<>(id));
    }

    @Override
    public <T> T getModel(BakedModelManager manager, CustomModelKey<T> key) {
        return manager.getStandaloneModel(((CustomModelKeyImpl<T>) key).backing());
    }
}
