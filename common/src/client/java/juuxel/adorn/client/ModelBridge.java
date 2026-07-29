package juuxel.adorn.client;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;

@InlineServices
public interface ModelBridge {
    <T> CustomModelKey<T> createModelKey(Identifier id, CustomModelKey.Type<T> type);

    <T> T getModel(ModelManager manager, CustomModelKey<T> key);

    @InlineServices.Getter
    static ModelBridge get() {
        return Services.load(ModelBridge.class);
    }
}
