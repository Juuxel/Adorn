package juuxel.adorn.entity;

import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;

public final class AdornEntities {
    public static final KeyedRegistrar<EntityType<?>> ENTITIES = RegistrarFactory.get().create(RegistryKeys.ENTITY_TYPE);

    public static final Registered<EntityType<SeatEntity>> SEAT = ENTITIES.register("seat",
        key -> EntityBridge.get().createEntityType(
            key,
            SeatEntity::new,
            SpawnGroup.MISC,
            builder -> builder.dimensions(0, 0)
        )
    );

    public static final Registered.WithKey<EntityType<?>, EntityType<ConeEntity>> CONE = ENTITIES.register("cone",
        key -> EntityBridge.get().createEntityType(
            key,
            ConeEntity::new,
            SpawnGroup.MISC,
            builder -> builder.dimensions(0.75f, 0.875f).maxTrackingRange(10)
        )
    );

    public static void init() {
    }
}
