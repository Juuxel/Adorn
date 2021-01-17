package juuxel.adorn.entity

import juuxel.adorn.Adorn
import juuxel.adorn.client.renderer.InvisibleEntityRenderer
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object AdornEntities {
    val ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Adorn.NAMESPACE)

    val SEAT: RegistryObject<EntityType<SeatEntity>> = ENTITIES.register("seat") {
        EntityType.Builder.create(::SeatEntity, SpawnGroup.MISC)
            .setDimensions(0f, 0f)
            .disableSaving()
            .build(null)
    }

    @OnlyIn(Dist.CLIENT)
    fun initClient() {
        RenderingRegistry.registerEntityRenderingHandler(SEAT.get(), ::InvisibleEntityRenderer)
    }
}
