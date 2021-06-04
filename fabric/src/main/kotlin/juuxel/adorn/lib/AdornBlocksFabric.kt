package juuxel.adorn.lib

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.CarpetedBlock
import juuxel.adorn.block.SneakClickHandler
import juuxel.adorn.client.SinkColorProvider
import juuxel.adorn.client.renderer.ShelfRenderer
import juuxel.adorn.client.renderer.TradingStationRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.DyedCarpetBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.item.BlockItem
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult

object AdornBlocksFabric {
    fun init() {
        UseBlockCallback.EVENT.register(
            UseBlockCallback { player, world, hand, hitResult ->
                val state = world.getBlockState(hitResult.blockPos)
                val block = state.block
                // Check that:
                // - the block is a sneak-click handler
                // - the player is sneaking
                // - the player isn't holding an item (for block item and bucket support)
                if (block is SneakClickHandler && player.isSneaking && player.getStackInHand(hand).isEmpty) {
                    block.onSneakClick(state, world, hitResult.blockPos, player, hand, hitResult)
                } else ActionResult.PASS
            }
        )

        UseBlockCallback.EVENT.register(
            UseBlockCallback { player, world, hand, hitResult ->
                val pos = hitResult.blockPos.offset(hitResult.side)
                val state = world.getBlockState(pos)
                val block = state.block
                val stack = player.getStackInHand(hand)
                val item = stack.item
                if (block is CarpetedBlock && block.canStateBeCarpeted(state) && item is BlockItem) {
                    val carpet = item.block
                    if (carpet is DyedCarpetBlock) {
                        world.setBlockState(
                            pos,
                            state.with(CarpetedBlock.CARPET, CarpetedBlock.CARPET.wrapOrNone(carpet.dyeColor))
                        )
                        val soundGroup = carpet.defaultState.soundGroup
                        world.playSound(
                            player,
                            pos,
                            soundGroup.placeSound,
                            SoundCategory.BLOCKS,
                            (soundGroup.volume + 1f) / 2f,
                            soundGroup.pitch * 0.8f
                        )
                        if (!player.abilities.creativeMode) stack.decrement(1)
                        player.swingHand(hand)
                        ActionResult.SUCCESS
                    }
                }

                ActionResult.PASS
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    @Environment(EnvType.CLIENT)
    private fun registerBer(type: BlockEntityType<*>, factory: () -> BlockEntityRenderer<*>) =
        BlockEntityRendererRegistry.INSTANCE.register(type as BlockEntityType<BlockEntity>) {
            factory() as BlockEntityRenderer<BlockEntity>
        }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        // BlockEntityRenderers
        registerBer(AdornBlockEntities.TRADING_STATION, ::TradingStationRenderer)
        registerBer(AdornBlockEntities.SHELF, ::ShelfRenderer)

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

        // BlockColorProviders
        ColorProviderRegistry.BLOCK.register(
            SinkColorProvider,
            AdornBlocks.OAK_KITCHEN_SINK,
            AdornBlocks.SPRUCE_KITCHEN_SINK,
            AdornBlocks.BIRCH_KITCHEN_SINK,
            AdornBlocks.JUNGLE_KITCHEN_SINK,
            AdornBlocks.ACACIA_KITCHEN_SINK,
            AdornBlocks.DARK_OAK_KITCHEN_SINK,
            AdornBlocks.CRIMSON_KITCHEN_SINK,
            AdornBlocks.WARPED_KITCHEN_SINK
        )
    }
}
