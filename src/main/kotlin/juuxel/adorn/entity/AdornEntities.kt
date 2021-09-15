package juuxel.adorn.entity

import juuxel.adorn.Adorn
import juuxel.adorn.client.renderer.InvisibleEntityRenderer
import juuxel.adorn.lib.RegistryHelper
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

object AdornEntities : RegistryHelper(Adorn.NAMESPACE) {
    val SEAT = register(
        Registry.ENTITY_TYPE,
        "seat",
        FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::SeatEntity)
            .dimensions(EntityDimensions.fixed(0f, 0f))
            .build()
    )

    fun init() {}

    @Environment(EnvType.CLIENT)
    fun initClient() {
        EntityRendererRegistry.INSTANCE.register(SEAT) { manager, _ ->
            InvisibleEntityRenderer(manager)
        }
    }
}
