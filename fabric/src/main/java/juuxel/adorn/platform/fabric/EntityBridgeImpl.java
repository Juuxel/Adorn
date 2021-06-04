package juuxel.adorn.platform.fabric;

import juuxel.adorn.entity.SeatEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public final class EntityBridgeImpl {
    public static EntityType<SeatEntity> createSeatType() {
        return FabricEntityTypeBuilder.create(SpawnGroup.MISC, SeatEntity::new)
            .dimensions(EntityDimensions.fixed(0f, 0f))
            .disableSaving()
            .build();
    }
}
