package juuxel.adorn.entity

import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.entity.EntityType

object AdornEntities {
    val ENTITIES: Registrar<EntityType<*>> = PlatformBridges.registrarFactory.entity()

    val SEAT: EntityType<SeatEntity> by ENTITIES.register("seat", PlatformBridges.entities::createSeatType)

    fun init() {}
}
