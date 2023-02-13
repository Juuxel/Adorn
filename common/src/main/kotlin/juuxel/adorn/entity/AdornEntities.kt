package juuxel.adorn.entity

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys

object AdornEntities {
    @JvmField
    val ENTITIES: Registrar<EntityType<*>> = RegistrarFactory.get().create(RegistryKeys.ENTITY_TYPE)

    val SEAT: EntityType<SeatEntity> by ENTITIES.register("seat", PlatformBridges.entities::createSeatType)

    fun init() {}
}
