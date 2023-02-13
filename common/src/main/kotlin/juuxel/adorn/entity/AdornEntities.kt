package juuxel.adorn.entity

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys

object AdornEntities {
    @JvmField
    val ENTITIES: Registrar<EntityType<*>> = PlatformBridges.registrarFactory.create(RegistryKeys.ENTITY_TYPE)

    val SEAT: EntityType<SeatEntity> by ENTITIES.register("seat", PlatformBridges.entities::createSeatType)

    fun init() {}
}
