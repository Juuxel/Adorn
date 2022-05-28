package juuxel.adorn.entity

import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.entity.EntityType
import net.minecraft.util.registry.Registry

object AdornEntities {
    @JvmField
    val ENTITIES: Registrar<EntityType<*>> = PlatformBridges.registrarFactory.create(Registry.ENTITY_TYPE_KEY)

    val SEAT: EntityType<SeatEntity> by ENTITIES.register("seat", PlatformBridges.entities::createSeatType)

    fun init() {}
}
