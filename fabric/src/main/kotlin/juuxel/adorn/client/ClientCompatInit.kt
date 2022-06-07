package juuxel.adorn.client

import juuxel.adorn.compat.BlockKind
import juuxel.adorn.compat.CompatBlocks
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer

object ClientCompatInit {
    fun init() {
        for (coffeeTable in CompatBlocks.get(BlockKind.COFFEE_TABLE)) {
            BlockRenderLayerMap.INSTANCE.putBlock(coffeeTable.get(), RenderLayer.getTranslucent())
        }
    }
}
