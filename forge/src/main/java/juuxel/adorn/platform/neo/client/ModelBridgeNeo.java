package juuxel.adorn.platform.neo.client;

import juuxel.adorn.client.CustomModelKey;
import juuxel.adorn.client.ModelBridge;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

public final class ModelBridgeNeo implements ModelBridge {
    @Override
    public CustomModelKey createModelKey(Identifier id) {
        return new CustomModelKeyImpl(new ModelIdentifier(id, "standalone"));
    }

    @Override
    public BakedModel getModel(BakedModelManager manager, CustomModelKey key) {
        return manager.getModel(((CustomModelKeyImpl) key).modelId());
    }
}
