package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.item.AdornTallBlockItem
import juuxel.adorn.util.VanillaWoodType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

object ModItems : PolyesterRegistry(Adorn.NAMESPACE) {
    // TODO: Update WoodFurnitureBuilder for this
    val CHAIRS: List<AdornTallBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(AdornTallBlockItem(ModBlocks.CHAIRS[it], Item.Settings().itemGroup(ItemGroup.DECORATIONS)))
    }

    fun init() {}
}
