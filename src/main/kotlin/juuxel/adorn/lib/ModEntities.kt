package juuxel.adorn.lib

import juuxel.adorn.Adorn
import juuxel.adorn.entity.SittingVehicleEntity
import juuxel.polyester.registry.PolyesterRegistry
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityCategory
import net.minecraft.entity.EntityDimensions
import net.minecraft.util.registry.Registry

object ModEntities : PolyesterRegistry(Adorn.NAMESPACE) {
    val SITTING_VEHICLE = register(
        Registry.ENTITY_TYPE,
        "sitting_vehicle",
        FabricEntityTypeBuilder.create(EntityCategory.MISC, ::SittingVehicleEntity)
            .size(EntityDimensions.fixed(0f, 0f))
            .build()
    )

    fun init() {}
}
