package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.block.*
import juuxel.adorn.block.entity.CarpetedBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.block.renderer.CarpetedBlockEntityRenderer
import juuxel.adorn.block.renderer.TradingStationRenderer
import juuxel.adorn.util.VanillaWoodType
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.item.BlockItem
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor

object ModBlocks : PolyesterRegistry(Adorn.NAMESPACE) {
    val SOFAS: List<SofaBlock> = DyeColor.values().map {
        registerBlock(SofaBlock(it.getName()))
    }

    val CHAIRS: List<ChairBlock> = VanillaWoodType.values().map {
        registerBlock(ChairBlock(it.id))
    }

    val TABLES: List<TableBlock> = VanillaWoodType.values().map {
        registerBlock(TableBlock(it.id))
    }

    val CARPETED_TABLES: List<TableBlock> = VanillaWoodType.values().map {
        registerBlock(TableBlock.Carpeted(it.id))
    }

    val KITCHEN_COUNTERS: List<KitchenCounterBlock> = VanillaWoodType.values().map {
        registerBlock(KitchenCounterBlock(it.id))
    }

    val KITCHEN_CUPBOARDS: List<KitchenCupboardBlock> = VanillaWoodType.values().map {
        registerBlock(KitchenCupboardBlock(it.id))
    }

    val CHIMNEY: ChimneyBlock = registerBlock(ChimneyBlock())

    val DRAWERS: List<DrawerBlock> = VanillaWoodType.values().map {
        registerBlock(DrawerBlock(it.id))
    }

    val TRADING_STATION: TradingStationBlock = registerBlock(TradingStationBlock())

    fun init() {
        UseBlockCallback.EVENT.register(UseBlockCallback { player, world, hand, hitResult ->
            val state = world.getBlockState(hitResult.blockPos)
            val block = state.block
            // Check that:
            // - the block is a sneak-click handler
            // - the player is sneaking
            // - the player isn't holding a block
            if (block is SneakClickHandler && player.isSneaking && player.getStackInHand(hand).item !is BlockItem) {
                block.onSneakClick(state, world, hitResult.blockPos, player)
            } else ActionResult.PASS
        })
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        BlockEntityRendererRegistry.INSTANCE.register(
            TradingStationBlockEntity::class.java,
            TradingStationRenderer()
        )

        BlockEntityRendererRegistry.INSTANCE.register(
            CarpetedBlockEntity::class.java,
            CarpetedBlockEntityRenderer()
        )
    }
}
