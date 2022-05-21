package juuxel.adorn.platform.forge.client

import juuxel.adorn.compat.BlockKind
import juuxel.adorn.compat.CompatBlocks
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

object ClientCompatInit {
    fun init(modBus: IEventBus) {
        modBus.addListener<FMLClientSetupEvent> {
            for (coffeeTable in CompatBlocks.get(BlockKind.COFFEE_TABLE)) {
                RenderLayers.setRenderLayer(coffeeTable.get(), RenderLayer.getTranslucent())
            }
        }
    }
}
