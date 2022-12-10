package juuxel.adorn.item

import juuxel.adorn.AdornCommon
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.BlockKind
import juuxel.adorn.block.BlockVariantSets
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.platform.ItemGroupBridge
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.block.Block
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.DyeColor
import net.minecraft.util.Rarity
import net.minecraft.util.math.Direction

object AdornItems {
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

    val ITEMS: Registrar<Item> = PlatformBridges.registrarFactory.create(RegistryKeys.ITEM)
    val GROUP by ItemGroupBridge.get().register(AdornCommon.id("items")) {
        icon { ItemStack(AdornBlocks.SOFAS[DyeColor.LIME]) }
        entries { _, entries, _ ->
            // TODO: Better ordering
            for (item in BlockVariantSets.items) {
                entries.add(item)
            }
            for (item in AdornBlocks.items) {
                entries.add(item)
            }
            for (item in ITEMS) {
                entries.add(item)
            }
        }
    }
    private val DRINK_FOOD_COMPONENT = drinkFoodComponentBuilder().build()

    private fun drinkFoodComponentBuilder() = FoodComponent.Builder().hunger(4).saturationModifier(0.3F).alwaysEdible()

    val STONE_ROD by ITEMS.register("stone_rod") { ItemWithDescription(Item.Settings()) }
    val MUG by ITEMS.register("mug") { ItemWithDescription(Item.Settings().maxCount(16)) }
    val HOT_CHOCOLATE by ITEMS.register("hot_chocolate") {
        DrinkInMugItem(Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1))
    }
    val SWEET_BERRY_JUICE by ITEMS.register("sweet_berry_juice") {
        DrinkInMugItem(Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1))
    }
    val GLOW_BERRY_TEA by ITEMS.register("glow_berry_tea") {
        DrinkInMugItem(
            Item.Settings()
                .food(drinkFoodComponentBuilder().statusEffect(StatusEffectInstance(StatusEffects.GLOWING, 400), 1.0f).build())
                .maxCount(1)
        )
    }
    val NETHER_WART_COFFEE by ITEMS.register("nether_wart_coffee") {
        DrinkInMugItem(Item.Settings().food(DRINK_FOOD_COMPONENT).maxCount(1))
    }

    val STONE_TORCH by ITEMS.register("stone_torch") {
        VerticallyAttachableBlockItemWithDescription(
            AdornBlocks.STONE_TORCH_GROUND,
            AdornBlocks.STONE_TORCH_WALL,
            Item.Settings(),
            attachmentDirection = Direction.DOWN
        )
    }

    val GUIDE_BOOK by ITEMS.register("guide_book") {
        AdornBookItem(AdornCommon.id("guide"), Item.Settings().rarity(Rarity.UNCOMMON))
    }

    val TRADERS_MANUAL by ITEMS.register("traders_manual") {
        AdornBookItem(AdornCommon.id("traders_manual"), Item.Settings())
    }

    fun init() {
        if (ConfigManager.config().client.showItemsInStandardGroups) {
            addToVanillaItemGroups()
        }
    }

    private fun addToVanillaItemGroups() {
        val itemGroups = ItemGroupBridge.get()
        itemGroups.addItems(ItemGroups.BUILDING_BLOCKS) {
            // TODO: Test on Forge what happens when you try to addAfter
            //  and the "after" item isn't in the group

            for (variant in BlockVariantSets.allVariants()) {
                val after = findLastBuildingBlockEntry(variant)

                if (after != null) {
                    val items = BUILDING_KINDS.mapNotNull {
                        BlockVariantSets.get(it, variant)?.get()
                    }
                    addAfter(after, items)
                } else {
                    for (kind in BUILDING_KINDS) {
                        BlockVariantSets.get(kind, variant)?.let { add(it.get()) }
                    }
                }
            }
        }
        itemGroups.addItems(ItemGroups.COLORED_BLOCKS) {
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
        itemGroups.addItems(ItemGroups.FUNCTIONAL) {
            for (variant in BlockVariantSets.allVariants()) {
                for (kind in FUNCTIONAL_KINDS) {
                    BlockVariantSets.get(kind, variant)?.let { add(it.get()) }
                }
            }

            // TODO: Better ordering
            for (item in AdornBlocks.items) {
                add(item)
            }
        }
        itemGroups.addItems(ItemGroups.FOOD_AND_DRINK) {
            add(MUG)
            add(HOT_CHOCOLATE)
            add(SWEET_BERRY_JUICE)
            add(GLOW_BERRY_TEA)
            add(NETHER_WART_COFFEE)
        }
        itemGroups.addItems(ItemGroups.INGREDIENTS) {
            add(STONE_ROD)
        }
        itemGroups.addItems(ItemGroups.TOOLS) {
            add(GUIDE_BOOK)
            add(TRADERS_MANUAL)
        }
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
