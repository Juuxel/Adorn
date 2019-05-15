package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.block.*
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.block.renderer.TradingStationRenderer
import juuxel.adorn.item.AdornWallBlockItem
import juuxel.adorn.util.BlockVariant
import juuxel.adorn.util.VanillaWoodType
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

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

    val STONE_TORCH_GROUND = registerBlock(StoneTorchBlock())

    val STONE_TORCH_WALL = registerBlock(
        StoneTorchBlock.Wall(
            Block.Settings.copy(STONE_TORCH_GROUND.unwrap()).dropsLike(STONE_TORCH_GROUND.unwrap())
        )
    )

    val STONE_TORCH_ITEM = register(
        Registry.ITEM,
        AdornWallBlockItem(
            STONE_TORCH_GROUND,
            STONE_TORCH_WALL,
            Item.Settings().itemGroup(ItemGroup.DECORATIONS)
        )
    )

    private val BUILDING_BLOCK_VARIANTS = sequence {
        yieldAll(VanillaWoodType.values().map { BlockVariant.Wood(Identifier("minecraft", it.id)) })
        yieldAll(BlockVariant.Stone.values().iterator())
    }.toList()

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
    }
}
