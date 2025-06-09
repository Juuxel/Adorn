package juuxel.adorn.client;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegisteredMap;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.StandaloneRegistrar;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.registry.RegistryKey;

public final class CustomModelKeys {
    public static final Registrar<CustomModelKey<?>> MODEL_KEYS = new StandaloneRegistrar<>();

    public static final RegisteredMap<RegistryKey<ConeVariant>, CustomModelKey<BlockStateModel>> CONES = Registrar.registerBy(
        ConeVariant.Keys.getAllBuiltinVariants(),
        variantKey -> blockStateModel("block/" + variantKey.getValue().getPath() + "_cone")
    );

    private static Registered<CustomModelKey<BlockStateModel>> blockStateModel(String id) {
        var key = ModelBridge.get().createModelKey(AdornCommon.id(id), CustomModelKey.BLOCK_STATE_MODEL);
        return MODEL_KEYS.register(id, () -> key);
    }
}
