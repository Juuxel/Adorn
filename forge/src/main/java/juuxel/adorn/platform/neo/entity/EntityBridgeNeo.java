package juuxel.adorn.platform.neo.entity;

import juuxel.adorn.entity.EntityBridge;
import net.minecraft.world.entity.Entity;

public final class EntityBridgeNeo implements EntityBridge {
    @Override
    public boolean isInFluid(Entity entity) {
        // See LivingEntity.shouldTravelInFluid
        return entity.isInWater() || entity.isInLava();

        // TODO: isInFluidType not yet reimplemented,
        //  see https://github.com/neoforged/NeoForge/blob/1fd4c72df409601cbc4a443c9da2645a84dc4ab7/src/main/java/net/neoforged/neoforge/common/extensions/IEntityExtension.java#L157
        // var fluidState = entity.level().getFluidState(entity.blockPosition());
        // return entity.isInWater() || entity.isInLava() || entity.isInFluidType(fluidState);
    }
}
