package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.entity.SittingVehicleEntity
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityCategory
import net.minecraft.entity.EntitySize
import net.minecraft.util.registry.Registry

object ModEntities : PolyesterRegistry(Adorn.NAMESPACE) {
    val SITTING_VEHICLE = register(
        Registry.ENTITY_TYPE,
        "sitting_vehicle",
        FabricEntityTypeBuilder.create(EntityCategory.MISC, ::SittingVehicleEntity)
            .size(EntitySize.constant(0f, 0f))
            .build()
    )

    fun init() {}
}
