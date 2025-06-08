package juuxel.adorn.client;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegisteredMap;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.StandaloneRegistrar;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.DyeColor;

public final class CustomModelKeys {
    public static final Registrar<CustomModelKey<?>> MODEL_KEYS = new StandaloneRegistrar<>();

    public static final RegisteredMap<DyeColor, CustomModelKey<BlockStateModel>> CONES = Registrar.registerBy(
        DyeColor.values(),
        color -> blockStateModel("block/" + color.asString() + "_cone")
    );

    private static Registered<CustomModelKey<BlockStateModel>> blockStateModel(String id) {
        var key = ModelBridge.get().createModelKey(AdornCommon.id(id), CustomModelKey.BLOCK_STATE_MODEL);
        return MODEL_KEYS.register(id, () -> key);
    }
}
