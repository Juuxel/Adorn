package juuxel.adorn.platform.fabric

import com.google.common.collect.ListMultimap
import juuxel.adorn.client.SinkColorProvider
import juuxel.adorn.compat.BlockKind
import juuxel.adorn.compat.ClientCompatLoader
import juuxel.adorn.lib.Registered
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.block.Block
import net.minecraft.client.render.RenderLayer

class ClientCompatLoaderImpl : ClientCompatLoader {
    override fun init(blocks: ListMultimap<BlockKind, Registered<Block>>) {
        for (kitchenSink in blocks.get(BlockKind.KITCHEN_SINK)) {
            ColorProviderRegistry.BLOCK.register(SinkColorProvider, kitchenSink.get())
        }

        for (coffeeTable in blocks.get(BlockKind.COFFEE_TABLE)) {
            BlockRenderLayerMap.INSTANCE.putBlock(coffeeTable.get(), RenderLayer.getTranslucent())
        }
    }
}
