package juuxel.adorn.entity

import juuxel.adorn.Adorn
import juuxel.adorn.lib.RegistryHelper
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

object AdornEntities : RegistryHelper(Adorn.NAMESPACE) {
    val SITTING_VEHICLE = register(
        Registry.ENTITY_TYPE,
        "sitting_vehicle",
        FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::SittingVehicleEntity)
            .size(EntityDimensions.fixed(0f, 0f))
            .build()
    )

    fun init() {}

    @Environment(EnvType.CLIENT)
    fun initClient() {
        EntityRendererRegistry.INSTANCE.register(SITTING_VEHICLE) { manager, _ -> InvisibleEntityRenderer(manager) }
    }
}
