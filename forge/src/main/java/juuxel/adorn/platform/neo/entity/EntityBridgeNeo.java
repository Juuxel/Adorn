package juuxel.adorn.platform.neo.entity;

import juuxel.adorn.entity.EntityBridge;
import net.minecraft.entity.Entity;

public final class EntityBridgeNeo implements EntityBridge {
    @Override
    public boolean isInFluid(Entity entity) {
        // See LivingEntity
        var fluidState = entity.getEntityWorld().getFluidState(entity.getBlockPos());
        return entity.isTouchingWater() || entity.isInLava() || entity.isInFluidType(fluidState);
    }
}
