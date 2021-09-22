package juuxel.adorn.platform.forge

import juuxel.adorn.entity.SeatEntity
import juuxel.adorn.platform.EntityBridge
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup

object EntityBridgeImpl : EntityBridge {
    override fun createSeatType(): EntityType<SeatEntity> =
        EntityType.Builder.create(::SeatEntity, SpawnGroup.MISC)
            .setDimensions(0f, 0f)
            .disableSaving()
            .build(null)
}
