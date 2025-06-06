package juuxel.adorn.client;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.lib.registry.RegisteredMap;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.StandaloneRegistrar;

public final class CustomModelKeys {
    public static final Registrar<CustomModelKey> MODEL_KEYS = new StandaloneRegistrar<>();

    public static final RegisteredMap<ConeVariant, CustomModelKey> CONES = Registrar.registerBy(
        ConeVariant.values(),
        variant -> MODEL_KEYS.register(variant.id(), () -> ModelBridge.get().createModelKey(AdornCommon.id("block/" + variant.id() + "_cone")))
    );
}
