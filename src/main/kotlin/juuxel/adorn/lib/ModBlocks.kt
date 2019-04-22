package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.block.*
import juuxel.adorn.util.WoodType
import net.minecraft.util.DyeColor
//import net.minecraft.util.registry.Registry

object ModBlocks : PolyesterRegistry(Adorn.NAMESPACE) {
    val SOFAS: List<SofaBlock> = DyeColor.values().map {
        registerBlock(SofaBlock(it.getName()))
    }

    val CHAIRS: List<ChairBlock> = WoodType.values().map {
        registerBlock(ChairBlock(it.id))
    }

    val TABLES: List<TableBlock> = WoodType.values().map {
        registerBlock(TableBlock(it.id))
    }

    val KITCHEN_COUNTERS: List<KitchenCounterBlock> = WoodType.values().map {
        registerBlock(KitchenCounterBlock(it.id))
    }

    val KITCHEN_CUPBOARDS: List<KitchenCupboardBlock> = WoodType.values().map {
        registerBlock(KitchenCupboardBlock(it.id))
    }

    val CHIMNEY: ChimneyBlock = registerBlock(ChimneyBlock())

    val DRAWERS: List<DrawerBlock> = WoodType.values().map {
        registerBlock(DrawerBlock(it.id))
    }

    fun init() {
        // Register here so they're only registered once
        //register(Registry.BLOCK_ENTITY, "kitchen_cupboard", KitchenCupboardBlock.BLOCK_ENTITY_TYPE)
        //register(Registry.BLOCK_ENTITY, "drawer", DrawerBlock.BLOCK_ENTITY_TYPE)
    }
}
