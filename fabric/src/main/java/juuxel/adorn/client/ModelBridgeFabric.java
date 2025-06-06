package juuxel.adorn.client;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;

public final class ModelBridgeFabric implements ModelBridge {
    @Override
    public CustomModelKey createModelKey(Identifier id) {
        return new CustomModelKeyImpl(id);
    }

    @Override
    public BakedModel getModel(BakedModelManager manager, CustomModelKey key) {
        return manager.getModel(((CustomModelKeyImpl) key).id());
    }

    public record CustomModelKeyImpl(Identifier id) implements CustomModelKey {
    }
}
