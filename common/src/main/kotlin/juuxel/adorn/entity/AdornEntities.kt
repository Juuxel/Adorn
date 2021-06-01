package juuxel.adorn.entity

import juuxel.adorn.platform.EntityBridge
import juuxel.adorn.platform.Registrar
import net.minecraft.entity.EntityType

object AdornEntities {
    val ENTITIES: Registrar<EntityType<*>> = Registrar.entity()

    val SEAT: EntityType<SeatEntity> by ENTITIES.register("seat", EntityBridge::createSeatType)

    fun init() {}
}
