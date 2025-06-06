package juuxel.adorn.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;

import java.util.function.Consumer;

public final class EntityBridgeFabric implements EntityBridge {
    @Override
    public boolean isInFluid(Entity entity) {
        return entity.isInFluid();
    }

    @Override
    public <T extends Entity> EntityType<T> createEntityType(RegistryKey<EntityType<?>> key, EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, Consumer<EntityType.Builder<T>> configurator) {
        var builder = EntityType.Builder.create(factory, spawnGroup);
        configurator.accept(builder);
        return builder.build();
    }
}
