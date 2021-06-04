package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import juuxel.adorn.entity.SeatEntity
import net.minecraft.entity.EntityType

object EntityBridge {
    @JvmStatic
    @ExpectPlatform
    fun createSeatType(): EntityType<SeatEntity> = expected
}
