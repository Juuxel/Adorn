package juuxel.adorn.entity;

import net.minecraft.entity.Entity;

public final class EntityBridgeFabric implements EntityBridge {
    @Override
    public boolean isInFluid(Entity entity) {
        return entity.isInFluid();
    }
}
