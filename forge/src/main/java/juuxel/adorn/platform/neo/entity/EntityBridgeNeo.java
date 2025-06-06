package juuxel.adorn.platform.neo.entity;

import juuxel.adorn.entity.EntityBridge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;

import java.util.function.Consumer;

public final class EntityBridgeNeo implements EntityBridge {
    @Override
    public boolean isInFluid(Entity entity) {
        // See LivingEntity
        var fluidState = entity.getWorld().getFluidState(entity.getBlockPos());
        return entity.isTouchingWater() || entity.isInLava() || entity.isInFluidType(fluidState);
    }

    @Override
    public <T extends Entity> EntityType<T> createEntityType(RegistryKey<EntityType<?>> key, EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, Consumer<EntityType.Builder<T>> configurator) {
        var builder = EntityType.Builder.create(factory, spawnGroup);
        configurator.accept(builder);
        return builder.build(key.getValue().toString());
    }
}
