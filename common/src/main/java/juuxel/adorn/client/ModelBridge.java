package juuxel.adorn.client;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;

@InlineServices
public interface ModelBridge {
    CustomModelKey createModelKey(Identifier id);

    BakedModel getModel(BakedModelManager manager, CustomModelKey key);

    @InlineServices.Getter
    static ModelBridge get() {
        return Services.load(ModelBridge.class);
    }
}
