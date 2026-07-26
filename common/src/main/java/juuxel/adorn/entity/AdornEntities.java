package juuxel.adorn.entity;

import juuxel.adorn.lib.registry.KeyedRegistrar;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.registries.Registries;

public final class AdornEntities {
    public static final KeyedRegistrar<EntityType<?>> ENTITIES = RegistrarFactory.get().create(Registries.ENTITY_TYPE);

    public static final Registered<EntityType<SeatEntity>> SEAT = ENTITIES.register("seat",
        key -> EntityType.Builder.of(SeatEntity::new, MobCategory.MISC)
            .sized(0f, 0f)
            .build(key));

    public static final Registered.WithKey<EntityType<?>, EntityType<ConeEntity>> CONE = ENTITIES.register("cone",
        key -> EntityType.Builder.of(ConeEntity::new, MobCategory.MISC)
            .noLootTable()
            .sized(0.75f, 0.875f)
            .clientTrackingRange(10)
            .build(key));

    public static void init() {
    }
}
