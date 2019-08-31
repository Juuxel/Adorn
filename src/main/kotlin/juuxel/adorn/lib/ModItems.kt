package juuxel.adorn.lib

import juuxel.adorn.Adorn
import juuxel.adorn.item.*
import juuxel.adorn.util.VanillaWoodType
import juuxel.adorn.util.colorFromComponents
import juuxel.polyester.registry.PolyesterRegistry
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.client.color.item.ItemColorProvider
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.nbt.AbstractNumberTag

object ModItems : PolyesterRegistry(Adorn.NAMESPACE) {
    val CHAIRS: List<ChairBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(ChairBlockItem(ModBlocks.CHAIRS[it]))
    }

    val TABLES: List<TableBlockItem> = VanillaWoodType.values().indices.map {
        registerItem(TableBlockItem(ModBlocks.TABLES[it]))
    }

    val STONE_ROD = registerItem("stone_rod", ItemWithDescription(Item.Settings().group(ItemGroup.MISC)))

    val STONE_TORCH = registerItem(
        "stone_torch",
        WallBlockItemWithDescription(
            ModBlocks.STONE_TORCH_GROUND,
            ModBlocks.STONE_TORCH_WALL,
            Item.Settings().group(ItemGroup.DECORATIONS)
        )
    )

    val COLOR_TEMPLATE = registerItem("color_template", ColorTemplateItem(Item.Settings().group(ItemGroup.MISC)))

    fun init() {
        with(FuelRegistry.INSTANCE) {
            add(ModTags.CHAIRS.item, 300)
            add(ModTags.DRAWERS.item, 300)
            add(ModTags.TABLES.item, 300)
            add(ModTags.WOODEN_POSTS.item, 300)
            add(ModTags.WOODEN_PLATFORMS.item, 300)
            add(ModTags.WOODEN_STEPS.item, 300)
            add(ModTags.WOODEN_SHELVES.item, 300)

            add(ModTags.SOFAS.item, 150)
        }
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        ColorProviderRegistry.ITEM.register(
            ItemColorProvider { stack, layer ->
                if (layer != 1) return@ItemColorProvider -1

                stack.tag?.getList(NbtKeys.COLOR_TEMPLATE_COLOR, NbtType.INT)?.let { colorTag ->
                    colorFromComponents(
                        (colorTag[0] as AbstractNumberTag).int,
                        (colorTag[1] as AbstractNumberTag).int,
                        (colorTag[2] as AbstractNumberTag).int
                    )
                } ?: -1
            }, COLOR_TEMPLATE
        )
    }
}
