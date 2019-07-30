package juuxel.adorn.lib

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.SneakClickHandler
import juuxel.adorn.api.util.BlockVariant
import juuxel.adorn.block.*
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.block.renderer.ShelfRenderer
import juuxel.adorn.block.renderer.TradingStationRenderer
import juuxel.adorn.util.VanillaWoodType
import juuxel.polyester.registry.PolyesterRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor

object ModBlocks : PolyesterRegistry(Adorn.NAMESPACE) {
    // BlockVariants
    private val WOODEN_VARIANTS = VanillaWoodType.values().map {
        BlockVariant.Wood(it.id)
    }

    private val BUILDING_BLOCK_VARIANTS = sequence {
        yieldAll(WOODEN_VARIANTS)
        yieldAll(BlockVariant.Stone.values().iterator())
    }.toList()

    // Blocks
    val SOFAS: List<SofaBlock> = DyeColor.values().map {
        registerBlock(SofaBlock(object : BlockVariant {
            override val variantName = it.asString()

            override fun createSettings() = Block.Settings.copy(Blocks.WHITE_WOOL)
        }))
    }

    val CHAIRS: List<ChairBlock> = WOODEN_VARIANTS.map {
        registerBlock(ChairBlock(it))
    }

    val TABLES: List<TableBlock> = WOODEN_VARIANTS.map {
        registerBlock(TableBlock(it))
    }

    val KITCHEN_COUNTERS: List<KitchenCounterBlock> = VanillaWoodType.values().map {
        registerBlock(KitchenCounterBlock(it.id))
    }

    val KITCHEN_CUPBOARDS: List<KitchenCupboardBlock> = VanillaWoodType.values().map {
        registerBlock(KitchenCupboardBlock(it.id))
    }

    val CHIMNEY: ChimneyBlock = registerBlock(ChimneyBlock())

    val DRAWERS: List<DrawerBlock> = WOODEN_VARIANTS.map {
        registerBlock(DrawerBlock(it))
    }

    val TRADING_STATION: TradingStationBlock = registerBlock(TradingStationBlock())

    val STONE_TORCH_GROUND = registerBlock(StoneTorchBlock())
    val STONE_TORCH_WALL = registerBlock(
        StoneTorchBlock.Wall(
            Block.Settings.copy(STONE_TORCH_GROUND).dropsLike(STONE_TORCH_GROUND)
        )
    )

    val POSTS = BUILDING_BLOCK_VARIANTS.map {
        registerBlock(PostBlock(it))
    }

    val PLATFORMS = BUILDING_BLOCK_VARIANTS.map {
        registerBlock(PlatformBlock(it))
    }

    val STEPS = BUILDING_BLOCK_VARIANTS.map {
        registerBlock(StepBlock(it))
    }

    val BUBBLE_CHIMNEY = registerBlock(BubbleChimneyBlock())

    val WOODEN_SHELVES: List<ShelfBlock> = WOODEN_VARIANTS.map {
        registerBlock(ShelfBlock(it))
    }

    val IRON_SHELF: ShelfBlock = registerBlock(
        ShelfBlock(object : BlockVariant {
            override val variantName = "iron"

            override fun createSettings() = Block.Settings.copy(Blocks.IRON_BARS)
        })
    )

    fun init() {
        UseBlockCallback.EVENT.register(UseBlockCallback { player, world, hand, hitResult ->
            val state = world.getBlockState(hitResult.blockPos)
            val block = state.block
            // Check that:
            // - the block is a sneak-click handler
            // - the player is sneaking
            // - the player isn't holding an item (for block item and bucket support)
            if (block is SneakClickHandler && player.isSneaking && player.getStackInHand(hand).isEmpty) {
                block.onSneakClick(state, world, hitResult.blockPos, player, hand, hitResult)
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
            ShelfBlockEntity::class.java,
            ShelfRenderer()
        )
    }
}
