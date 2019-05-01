package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.block.*
import juuxel.adorn.item.AdornWallBlockItem
import juuxel.adorn.util.BlockVariant
import juuxel.adorn.util.VanillaWoodType
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
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
        // Register here so they're only registered once
        register(Registry.BLOCK_ENTITY, "kitchen_cupboard", KitchenCupboardBlock.BLOCK_ENTITY_TYPE)
        register(Registry.BLOCK_ENTITY, "drawer", DrawerBlock.BLOCK_ENTITY_TYPE)
    }

    private fun getVariantContentName(variantId: Identifier): String =
        when (variantId.namespace) {
            "minecraft" -> variantId.path
            else -> variantId.namespace + '_' + variantId.path
        }
}
