package juuxel.adorn.entity;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKey;

import java.util.function.Consumer;

@InlineServices
public interface EntityBridge {
    boolean isInFluid(Entity entity);

    <T extends Entity> EntityType<T> createEntityType(RegistryKey<EntityType<?>> key, EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, Consumer<EntityType.Builder<T>> configurator);

    @InlineServices.Getter
    static EntityBridge get() {
        return Services.load(EntityBridge.class);
    }
}
