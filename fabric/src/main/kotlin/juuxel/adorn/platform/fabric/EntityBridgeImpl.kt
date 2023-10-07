package juuxel.adorn.platform.fabric

import juuxel.adorn.entity.SeatEntity
import juuxel.adorn.platform.EntityBridge
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup

object EntityBridgeImpl : EntityBridge {
    override fun createSeatType(): EntityType<SeatEntity> =
        FabricEntityTypeBuilder.create(SpawnGroup.MISC) { type, world -> SeatEntity(type, world) }
            .dimensions(EntityDimensions.fixed(0f, 0f))
            .build()
}
