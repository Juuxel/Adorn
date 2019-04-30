package juuxel.adorn.lib

import io.github.juuxel.polyester.block.WoodType
import io.github.juuxel.polyester.plugin.PolyesterPluginManager
import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.block.*
import net.minecraft.util.DyeColor
import net.minecraft.util.registry.Registry

object ModBlocks : PolyesterRegistry(Adorn.NAMESPACE) {
    val SOFAS: List<SofaBlock> = DyeColor.values().map {
        registerBlock(SofaBlock(it.getName()))
    }

    private val WOOD_TYPES = PolyesterPluginManager.plugins.flatMap { it.woodTypes }.map {
        getVariantContentName(it)
    }

    val CHAIRS: List<ChairBlock> = WOOD_TYPES.map {
        registerBlock(ChairBlock(it))
    }

    val TABLES: List<TableBlock> = WOOD_TYPES.map {
        registerBlock(TableBlock(it))
    }

    val KITCHEN_COUNTERS: List<KitchenCounterBlock> = WOOD_TYPES.map {
        registerBlock(KitchenCounterBlock(it))
    }

    val KITCHEN_CUPBOARDS: List<KitchenCupboardBlock> = WOOD_TYPES.map {
        registerBlock(KitchenCupboardBlock(it))
    }

    val CHIMNEY: ChimneyBlock = registerBlock(ChimneyBlock())

    val DRAWERS: List<DrawerBlock> = WOOD_TYPES.map {
        registerBlock(DrawerBlock(it))
    }

    fun init() {
        // Register here so they're only registered once
        register(Registry.BLOCK_ENTITY, "kitchen_cupboard", KitchenCupboardBlock.BLOCK_ENTITY_TYPE)
        register(Registry.BLOCK_ENTITY, "drawer", DrawerBlock.BLOCK_ENTITY_TYPE)
    }

    private fun getVariantContentName(woodType: WoodType): String =
        when (woodType.id.namespace) {
            "minecraft" -> woodType.id.path
            else -> woodType.id.namespace + '_' + woodType.id.path
        }
}
