package juuxel.adorn.block

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.SneakClickHandler
import juuxel.adorn.api.util.BlockVariant
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
import net.minecraft.block.CarpetBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult

object AdornBlocks : PolyesterRegistry(Adorn.NAMESPACE) {
    // BlockVariants
    private val WOODEN_VARIANTS = VanillaWoodType.values().map {
        BlockVariant.Wood(it.id)
    }

    private val BUILDING_BLOCK_VARIANTS = sequence {
        yieldAll(WOODEN_VARIANTS)
        yieldAll(BlockVariant.VanillaStone.values().iterator())
    }.toList()

    // Blocks
    val SOFAS: List<SofaBlock> = BlockVariant.WOOLS.values.map {
        registerBlock(SofaBlock(it))
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

    val WOODEN_SHELVES: List<ShelfBlock> = WOODEN_VARIANTS.map {
        registerBlock(ShelfBlock(it))
    }

    val IRON_SHELF: ShelfBlock = registerBlock(ShelfBlock(BlockVariant.IRON))

    val BRICK_CHIMNEY: ChimneyBlock = registerBlock("chimney", ChimneyBlock(), ItemGroup.DECORATIONS)
    val STONE_BRICK_CHIMNEY = registerBlock("stone_brick_chimney", ChimneyBlock(), ItemGroup.DECORATIONS)
    val NETHER_BRICK_CHIMNEY = registerBlock("nether_brick_chimney", ChimneyBlock(), ItemGroup.DECORATIONS)
    val RED_NETHER_BRICK_CHIMNEY = registerBlock("red_nether_brick_chimney", ChimneyBlock(), ItemGroup.DECORATIONS)
    val PRISMARINE_CHIMNEY = registerBlock("bubble_chimney", PrismarineChimneyBlock(), ItemGroup.DECORATIONS)

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

        UseBlockCallback.EVENT.register(UseBlockCallback { player, world, hand, hitResult ->
            val pos = hitResult.blockPos.offset(hitResult.side)
            val state = world.getBlockState(pos)
            val block = state.block
            val stack = player.getStackInHand(hand)
            val item = stack.item
            if (block is CarpetedBlock && block.canStateBeCarpeted(state) && item is BlockItem) {
                val carpet = item.block
                if (carpet is CarpetBlock) {
                    world.setBlockState(pos, state.with(CarpetedBlock.CARPET, CarpetedBlock.CARPET.wrapOrNone(carpet.color)))
                    val soundGroup = carpet.defaultState.soundGroup
                    world.playSound(player, pos, soundGroup.placeSound, SoundCategory.BLOCKS, (soundGroup.volume + 1f) / 2f, soundGroup.pitch * 0.8f)
                    if (!player.abilities.creativeMode) stack.decrement(1)
                    ActionResult.SUCCESS
                }
            }

            ActionResult.PASS
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
