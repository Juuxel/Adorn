package juuxel.adorn.lib

import juuxel.adorn.CommonEventHandlers
import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.SneakClickHandler
import juuxel.adorn.block.entity.BrewerBlockEntityFabric
import juuxel.adorn.block.entity.KitchenSinkBlockEntityFabric
import juuxel.adorn.block.variant.BlockKind
import juuxel.adorn.block.variant.BlockVariantSets
import juuxel.adorn.client.renderer.KitchenSinkRendererFabric
import juuxel.adorn.client.renderer.ShelfRenderer
import juuxel.adorn.client.renderer.TradingStationRenderer
import juuxel.adorn.client.renderer.design.DynamicFurnitureRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.ActionResult

object AdornBlocksFabric {
    fun init() {
        UseBlockCallback.EVENT.register { player, world, hand, hitResult ->
            val state = world.getBlockState(hitResult.blockPos)
            val block = state.block
            // Check that:
            // - the block is a sneak-click handler
            // - the player is sneaking
            // - the player isn't holding an item (for block item and bucket support)
            if (block is SneakClickHandler && player.isSneaking && player.getStackInHand(hand).isEmpty) {
                block.onSneakClick(state, world, hitResult.blockPos, player, hand, hitResult)
            } else {
                ActionResult.PASS
            }
        }

        UseBlockCallback.EVENT.register(CommonEventHandlers::handleCarpets)
        FluidStorage.SIDED.registerForBlockEntity(
            { brewer, _ -> (brewer as BrewerBlockEntityFabric).fluidStorage },
            AdornBlockEntities.BREWER
        )
    }

    fun afterRegister() {
        for (kitchenSink in BlockVariantSets.get(BlockKind.KITCHEN_SINK)) {
            FluidStorage.SIDED.registerForBlocks(KitchenSinkBlockEntityFabric.FLUID_STORAGE_PROVIDER, kitchenSink.get())
        }
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        @Suppress("UNCHECKED_CAST")
        fun <T : BlockEntity> BlockEntityType<*>.forceType(): BlockEntityType<T> =
            this as BlockEntityType<T>

        // BlockEntityRenderers
        BlockEntityRendererRegistry.register(AdornBlockEntities.TRADING_STATION, ::TradingStationRenderer)
        BlockEntityRendererRegistry.register(AdornBlockEntities.SHELF, ::ShelfRenderer)
        BlockEntityRendererRegistry.register(AdornBlockEntities.KITCHEN_SINK.forceType(), ::KitchenSinkRendererFabric)
        BlockEntityRendererRegistry.register(AdornBlockEntities.DYNAMIC_FURNITURE, ::DynamicFurnitureRenderer)

        // RenderLayers
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutout(),
            AdornBlocks.TRADING_STATION,
            AdornBlocks.STONE_TORCH_GROUND,
            AdornBlocks.STONE_TORCH_WALL,
            AdornBlocks.CHAIN_LINK_FENCE,
            AdornBlocks.STONE_LADDER,
            AdornBlocks.CANDLELIT_LANTERN
        )

        for ((_, block) in AdornBlocks.DYED_CANDLELIT_LANTERNS) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout())
        }

        for (coffeeTable in BlockVariantSets.get(BlockKind.COFFEE_TABLE)) {
            BlockRenderLayerMap.INSTANCE.putBlock(coffeeTable.get(), RenderLayer.getTranslucent())
        }
    }
}
