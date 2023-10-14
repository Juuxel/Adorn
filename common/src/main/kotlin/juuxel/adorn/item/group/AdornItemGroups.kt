package juuxel.adorn.item.group

import juuxel.adorn.AdornCommon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.variant.BlockKind
import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.block.variant.BlockVariantSets
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.item.AdornItems
import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import juuxel.adorn.platform.ItemGroupBridge
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.Util

object AdornItemGroups {
    val ITEM_GROUPS: Registrar<ItemGroup> = RegistrarFactory.get().create(RegistryKeys.ITEM_GROUP)

    // Block kinds for each vanilla item group
    private val BUILDING_KINDS: List<BlockKind> = listOf(
        BlockKind.POST,
        BlockKind.PLATFORM,
        BlockKind.STEP,
    )
    private val FUNCTIONAL_KINDS: List<BlockKind> = listOf(
        BlockKind.CHAIR,
        BlockKind.TABLE,
        BlockKind.DRAWER,
        BlockKind.KITCHEN_COUNTER,
        BlockKind.KITCHEN_CUPBOARD,
        BlockKind.KITCHEN_SINK,
        BlockKind.SHELF,
        BlockKind.COFFEE_TABLE,
        BlockKind.BENCH,
    )

    private const val GROUP_ID = "items"
    val GROUP: ItemGroup by ITEM_GROUPS.register(GROUP_ID) {
        ItemGroupBridge.get().builder()
            .displayName(Text.translatable(Util.createTranslationKey("itemGroup", AdornCommon.id(GROUP_ID))))
            .icon { ItemStack(AdornBlocks.SOFAS[DyeColor.LIME]) }
            .entries { _, entries ->
                val context = ItemGroupBuildContext { entries.add(it) }
                with(context) {
                    when (ConfigManager.config().groupItems) {
                        ItemGroupingOption.BY_MATERIAL -> addByKinds(BlockKind.values().toList())
                        ItemGroupingOption.BY_SHAPE -> {
                            for (kind in BlockKind.values()) {
                                for (block in BlockVariantSets.get(kind)) {
                                    add(block.get())
                                }
                            }
                        }
                    }
                    addColoredBlocks()
                    addChimneys()
                    addFences()
                    addCopperPipes()
                    addCrates()
                    addMiscDecorations()
                    add(AdornBlocks.TRADING_STATION)
                    add(AdornBlocks.BREWER)
                    addFoodAndDrink()
                    addIngredients()
                    addTools()
                }
            }
            .build()
    }

    fun init() {
        if (ConfigManager.config().client.showItemsInStandardGroups) {
            addToVanillaItemGroups()
        }
    }

    private fun addToVanillaItemGroups() {
        val itemGroups = ItemGroupBridge.get()
        itemGroups.addItems(ItemGroups.BUILDING_BLOCKS) {
            for (variant in BlockVariantSets.allVariants()) {
                val after = findLastBuildingBlockEntry(variant)

                if (after != null) {
                    val items = BUILDING_KINDS.mapNotNull {
                        BlockVariantSets.get(it, variant)?.get()
                    }
                    addAfter(after, items)
                } else {
                    addByKinds(variant, BUILDING_KINDS)
                }
            }

            addAfter(Items.CUT_COPPER_SLAB, AdornBlocks.COPPER_PIPE)
            addAfter(Items.EXPOSED_CUT_COPPER_SLAB, AdornBlocks.EXPOSED_COPPER_PIPE)
            addAfter(Items.WEATHERED_CUT_COPPER_SLAB, AdornBlocks.WEATHERED_COPPER_PIPE)
            addAfter(Items.OXIDIZED_CUT_COPPER_SLAB, AdornBlocks.OXIDIZED_COPPER_PIPE)
            addAfter(Items.WAXED_CUT_COPPER_SLAB, AdornBlocks.WAXED_COPPER_PIPE)
            addAfter(Items.WAXED_EXPOSED_CUT_COPPER_SLAB, AdornBlocks.WAXED_EXPOSED_COPPER_PIPE)
            addAfter(Items.WAXED_WEATHERED_CUT_COPPER_SLAB, AdornBlocks.WAXED_WEATHERED_COPPER_PIPE)
            addAfter(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB, AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE)
        }
        itemGroups.addItems(ItemGroups.COLORED_BLOCKS) {
            addColoredBlocks()
        }
        itemGroups.addItems(ItemGroups.FUNCTIONAL) {
            addByKinds(FUNCTIONAL_KINDS)
            addColoredBlocks()
            addChimneys()
            addFences()
            addCrates()
            addMiscDecorations()
            add(AdornBlocks.TRADING_STATION)
            add(AdornBlocks.BREWER)
        }
        itemGroups.addItems(ItemGroups.FOOD_AND_DRINK) {
            addFoodAndDrink()
        }
        itemGroups.addItems(ItemGroups.INGREDIENTS) {
            addIngredients()
        }
        itemGroups.addItems(ItemGroups.TOOLS) {
            addTools()
        }
    }

    private fun ItemGroupBuildContext.addByKinds(kinds: List<BlockKind>) {
        for (variant in BlockVariantSets.allVariants()) {
            addByKinds(variant, kinds)
        }
    }

    private fun ItemGroupBuildContext.addByKinds(variant: BlockVariant, kinds: List<BlockKind>) {
        for (kind in kinds) {
            BlockVariantSets.get(kind, variant)?.let { add(it.get()) }
        }
    }

    private fun ItemGroupBuildContext.addColoredBlocks() {
        for ((_, sofa) in AdornBlocks.SOFAS) {
            add(sofa)
        }
        for ((_, lamp) in AdornBlocks.TABLE_LAMPS) {
            add(lamp)
        }
        add(AdornBlocks.CANDLELIT_LANTERN)
        for ((_, lantern) in AdornBlocks.DYED_CANDLELIT_LANTERNS) {
            add(lantern)
        }
    }

    private fun ItemGroupBuildContext.addCrates() {
        add(AdornBlocks.CRATE)
        add(AdornBlocks.APPLE_CRATE)
        add(AdornBlocks.WHEAT_CRATE)
        add(AdornBlocks.CARROT_CRATE)
        add(AdornBlocks.POTATO_CRATE)
        add(AdornBlocks.MELON_CRATE)
        add(AdornBlocks.WHEAT_SEED_CRATE)
        add(AdornBlocks.MELON_SEED_CRATE)
        add(AdornBlocks.PUMPKIN_SEED_CRATE)
        add(AdornBlocks.BEETROOT_CRATE)
        add(AdornBlocks.BEETROOT_SEED_CRATE)
        add(AdornBlocks.SWEET_BERRY_CRATE)
        add(AdornBlocks.COCOA_BEAN_CRATE)
        add(AdornBlocks.NETHER_WART_CRATE)
        add(AdornBlocks.SUGAR_CANE_CRATE)
        add(AdornBlocks.EGG_CRATE)
        add(AdornBlocks.HONEYCOMB_CRATE)
        add(AdornBlocks.LIL_TATER_CRATE)
    }

    private fun ItemGroupBuildContext.addChimneys() {
        add(AdornBlocks.BRICK_CHIMNEY)
        add(AdornBlocks.STONE_BRICK_CHIMNEY)
        add(AdornBlocks.NETHER_BRICK_CHIMNEY)
        add(AdornBlocks.RED_NETHER_BRICK_CHIMNEY)
        add(AdornBlocks.COBBLESTONE_CHIMNEY)
        add(AdornBlocks.PRISMARINE_CHIMNEY)
        add(AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY)
        add(AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY)
    }

    private fun ItemGroupBuildContext.addFences() {
        add(AdornBlocks.PICKET_FENCE)
        add(AdornBlocks.CHAIN_LINK_FENCE)
    }

    private fun ItemGroupBuildContext.addCopperPipes() {
        add(AdornBlocks.COPPER_PIPE)
        add(AdornBlocks.EXPOSED_COPPER_PIPE)
        add(AdornBlocks.WEATHERED_COPPER_PIPE)
        add(AdornBlocks.OXIDIZED_COPPER_PIPE)
        add(AdornBlocks.WAXED_COPPER_PIPE)
        add(AdornBlocks.WAXED_EXPOSED_COPPER_PIPE)
        add(AdornBlocks.WAXED_WEATHERED_COPPER_PIPE)
        add(AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE)
    }

    private fun ItemGroupBuildContext.addFoodAndDrink() {
        add(AdornItems.MUG)
        add(AdornItems.HOT_CHOCOLATE)
        add(AdornItems.SWEET_BERRY_JUICE)
        add(AdornItems.GLOW_BERRY_TEA)
        add(AdornItems.NETHER_WART_COFFEE)
    }

    private fun ItemGroupBuildContext.addIngredients() {
        if (this is ItemGroupModifyContext) {
            addAfter(after = Items.STICK, listOf(AdornItems.STONE_ROD))
            addAfter(after = Items.IRON_NUGGET, AdornItems.COPPER_NUGGET)
        } else {
            add(AdornItems.STONE_ROD)
            add(AdornItems.COPPER_NUGGET)
        }
    }

    private fun ItemGroupBuildContext.addTools() {
        add(AdornItems.WATERING_CAN)
        add(AdornItems.GUIDE_BOOK)
        add(AdornItems.TRADERS_MANUAL)
    }

    private fun ItemGroupBuildContext.addMiscDecorations() {
        add(AdornItems.STONE_TORCH)
        add(AdornBlocks.STONE_LADDER)
    }

    private fun findLastBuildingBlockEntry(variant: BlockVariant): Block? {
        // Buttons are the last entry in "building blocks" for each material currently,
        // then walls and finally slabs
        return findBaseBlock(variant, "button")
            ?: findBaseBlock(variant, "wall")
            ?: findBaseBlock(variant, "slab")
            ?: findBaseBlock(variant, null)
    }

    private fun findBaseBlock(variant: BlockVariant, suffix: String?): Block? {
        val variantId = variant.nameAsIdentifier()
        val buttonId = if (suffix != null) variantId.withPath { it + "_$suffix" } else variantId

        if (Registries.BLOCK.containsId(buttonId)) {
            return Registries.BLOCK[buttonId]
        }

        return null
    }
}
