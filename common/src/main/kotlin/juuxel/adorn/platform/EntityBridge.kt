package juuxel.adorn.platform

import juuxel.adorn.entity.SeatEntity
import net.minecraft.entity.EntityType

interface EntityBridge {
    fun createSeatType(): EntityType<SeatEntity>
}
