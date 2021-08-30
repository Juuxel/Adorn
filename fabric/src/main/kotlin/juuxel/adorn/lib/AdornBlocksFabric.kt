package juuxel.adorn.lib

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.CarpetedBlock
import juuxel.adorn.block.SneakClickHandler
import juuxel.adorn.block.SofaBlock
import juuxel.adorn.client.SinkColorProvider
import juuxel.adorn.client.renderer.ShelfRenderer
import juuxel.adorn.client.renderer.TradingStationRenderer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.DyedCarpetBlock
import net.minecraft.client.render.RenderLayer
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

        EntitySleepEvents.ALLOW_BED.register(EntitySleepEvents.AllowBed { _, _, state, _ ->
            if (state.block is SofaBlock) ActionResult.SUCCESS
            else ActionResult.PASS
        })

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register(EntitySleepEvents.AllowSettingSpawn { player, sleepingPos ->
            player.world.getBlockState(sleepingPos).block !is SofaBlock
        })

        EntitySleepEvents.ALLOW_SLEEP_TIME.register(EntitySleepEvents.AllowSleepTime { player, sleepingPos, _ ->
            if (player.world.isDay && player.world.getBlockState(sleepingPos).block is SofaBlock) ActionResult.SUCCESS
            else ActionResult.PASS
        })

        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register(EntitySleepEvents.ModifySleepingDirection { entity, sleepingPos, sleepingDirection ->
            if (entity.world.getBlockState(sleepingPos).block is SofaBlock)
                SofaBlock.getSleepingDirection(entity.world, sleepingPos, ignoreNeighbors = true)
            else sleepingDirection
        })

        EntitySleepEvents.ALLOW_RESETTING_TIME.register(EntitySleepEvents.AllowResettingTime { player ->
            val pos = player.sleepingPosition.orElse(null) ?: return@AllowResettingTime true

            if (player.world.getBlockState(pos).block is SofaBlock) {
                if (player.world.isDay) false
                else player.world.gameRules.getBoolean(AdornGameRules.SKIP_NIGHT_ON_SOFAS)
            } else {
                true
            }
        })
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        // BlockEntityRenderers
        BlockEntityRendererRegistry.INSTANCE.register(AdornBlockEntities.TRADING_STATION, ::TradingStationRenderer)
        BlockEntityRendererRegistry.INSTANCE.register(AdornBlockEntities.SHELF, ::ShelfRenderer)

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
