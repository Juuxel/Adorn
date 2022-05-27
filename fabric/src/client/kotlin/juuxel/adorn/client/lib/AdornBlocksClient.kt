package juuxel.adorn.client.lib

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.client.renderer.KitchenSinkRendererFabric
import juuxel.adorn.client.renderer.ShelfRenderer
import juuxel.adorn.client.renderer.TradingStationRenderer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer

object AdornBlocksClient {
    fun init() {
        @Suppress("UNCHECKED_CAST")
        fun <T : BlockEntity> BlockEntityType<*>.forceType(): BlockEntityType<T> =
            this as BlockEntityType<T>

        // BlockEntityRenderers
        BlockEntityRendererRegistry.register(AdornBlockEntities.TRADING_STATION, ::TradingStationRenderer)
        BlockEntityRendererRegistry.register(AdornBlockEntities.SHELF, ::ShelfRenderer)
        BlockEntityRendererRegistry.register(AdornBlockEntities.KITCHEN_SINK.forceType(), ::KitchenSinkRendererFabric)

        // RenderLayers
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutout(),
            AdornBlocks.TRADING_STATION,
            AdornBlocks.STONE_TORCH_GROUND,
            AdornBlocks.STONE_TORCH_WALL,
            AdornBlocks.CHAIN_LINK_FENCE,
            AdornBlocks.STONE_LADDER
        )

        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getTranslucent(),
            AdornBlocks.OAK_COFFEE_TABLE,
            AdornBlocks.SPRUCE_COFFEE_TABLE,
            AdornBlocks.BIRCH_COFFEE_TABLE,
            AdornBlocks.JUNGLE_COFFEE_TABLE,
            AdornBlocks.ACACIA_COFFEE_TABLE,
            AdornBlocks.DARK_OAK_COFFEE_TABLE,
            AdornBlocks.CRIMSON_COFFEE_TABLE,
            AdornBlocks.WARPED_COFFEE_TABLE
        )
    }
}
