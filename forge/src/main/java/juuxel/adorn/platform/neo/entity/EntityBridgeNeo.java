package juuxel.adorn.platform.neo.entity;

import juuxel.adorn.entity.EntityBridge;
import net.minecraft.world.entity.Entity;

public final class EntityBridgeNeo implements EntityBridge {
    @Override
    public boolean isInFluid(Entity entity) {
        // See LivingEntity
        var fluidState = entity.level().getFluidState(entity.blockPosition());
        return entity.isInWater() || entity.isInLava() || entity.isInFluidType(fluidState);
    }
}
