package juuxel.adorn.client

import juuxel.adorn.compat.BlockKind
import juuxel.adorn.compat.CompatBlocks
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.render.RenderLayer

object ClientCompatInit {
    fun init() {
        for (kitchenSink in CompatBlocks.get(BlockKind.KITCHEN_SINK)) {
            ColorProviderRegistry.BLOCK.register(SinkColorProvider, kitchenSink.get())
        }

        for (coffeeTable in CompatBlocks.get(BlockKind.COFFEE_TABLE)) {
            BlockRenderLayerMap.INSTANCE.putBlock(coffeeTable.get(), RenderLayer.getTranslucent())
        }
    }
}
