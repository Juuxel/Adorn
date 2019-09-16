package juuxel.adorn.entity

import juuxel.adorn.Adorn
import juuxel.adorn.lib.RegistryHelper
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityCategory
import net.minecraft.entity.EntityDimensions
import net.minecraft.util.registry.Registry

object AdornEntities : RegistryHelper(Adorn.NAMESPACE) {
    val SITTING_VEHICLE = register(
        Registry.ENTITY_TYPE,
        "sitting_vehicle",
        FabricEntityTypeBuilder.create(EntityCategory.MISC, ::SittingVehicleEntity)
            .size(EntityDimensions.fixed(0f, 0f))
            .build()
    )

    fun init() {}
}
