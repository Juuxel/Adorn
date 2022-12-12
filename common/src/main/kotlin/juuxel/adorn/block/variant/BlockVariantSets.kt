package juuxel.adorn.block.variant

import com.google.common.collect.ListMultimap
import com.google.common.collect.MultimapBuilder
import juuxel.adorn.block.BenchBlock
import juuxel.adorn.block.ChairBlock
import juuxel.adorn.block.CoffeeTableBlock
import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.KitchenCounterBlock
import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.block.KitchenSinkBlock
import juuxel.adorn.block.PlatformBlock
import juuxel.adorn.block.PostBlock
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.StepBlock
import juuxel.adorn.block.TableBlock
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.lib.Registered
import juuxel.adorn.lib.RegistryHelper
import net.minecraft.block.Block

object BlockVariantSets : RegistryHelper() {
    private val variantSets: MutableList<BlockVariantSet> = mutableListOf(MinecraftBlockVariants)
    private val blocksByKind: ListMultimap<BlockKind, Registered<Block>> =
        MultimapBuilder.enumKeys(BlockKind::class.java)
            .arrayListValues()
            .build()
    private val blocksByVariant: ListMultimap<BlockVariant, Registered<Block>> =
        MultimapBuilder.linkedHashKeys()
            .arrayListValues()
            .build()
    private val blocksByKindVariant: MutableMap<Pair<BlockKind, BlockVariant>, Registered<Block>> =
        LinkedHashMap()

    fun allVariants(): Set<BlockVariant> = blocksByVariant.keySet()

    fun add(variantSet: BlockVariantSet) {
        variantSets += variantSet
    }

    fun get(kind: BlockKind): List<Registered<Block>> =
        blocksByKind[kind]

    fun get(kind: BlockKind, variant: BlockVariant): Registered<Block>? =
        blocksByKindVariant[kind to variant]

    fun register() {
        val woodVariants = variantSets.flatMap(BlockVariantSet::woodVariants)
        val stoneVariants = variantSets.flatMap(BlockVariantSet::stoneVariants)
        val allVariants = woodVariants + stoneVariants
        register(BlockKind.CHAIR, woodVariants)
        register(BlockKind.TABLE, woodVariants)
        register(BlockKind.DRAWER, woodVariants)
        register(BlockKind.KITCHEN_COUNTER, woodVariants)
        register(BlockKind.KITCHEN_CUPBOARD, woodVariants)
        register(BlockKind.KITCHEN_SINK, woodVariants)
        register(BlockKind.POST, allVariants)
        register(BlockKind.PLATFORM, allVariants)
        register(BlockKind.STEP, allVariants)
        register(BlockKind.SHELF, woodVariants)
        register(BlockKind.COFFEE_TABLE, woodVariants)
        register(BlockKind.BENCH, woodVariants)

        for (set in variantSets) {
            set.addVariants { variant, kinds ->
                for (kind in kinds) {
                    register(kind, variant)
                }
            }
        }
    }

    private fun register(kind: BlockKind, variants: List<BlockVariant>) {
        for (variant in variants) {
            register(kind, variant)
        }
    }

    private fun register(kind: BlockKind, variant: BlockVariant) {
        val registered = when (kind) {
            BlockKind.CHAIR -> registerChair(variant)
            BlockKind.TABLE -> registerTable(variant)
            BlockKind.DRAWER -> registerDrawer(variant)
            BlockKind.KITCHEN_COUNTER -> registerKitchenCounter(variant)
            BlockKind.KITCHEN_CUPBOARD -> registerKitchenCupboard(variant)
            BlockKind.KITCHEN_SINK -> registerKitchenSink(variant)
            BlockKind.POST -> registerPost(variant)
            BlockKind.PLATFORM -> registerPlatform(variant)
            BlockKind.STEP -> registerStep(variant)
            BlockKind.SHELF -> registerShelf(variant)
            BlockKind.COFFEE_TABLE -> registerCoffeeTable(variant)
            BlockKind.BENCH -> registerBench(variant)
        }
        blocksByKind.put(kind, registered)
        blocksByVariant.put(variant, registered)
        blocksByKindVariant[kind to variant] = registered
    }

    private fun registerPost(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_post") { PostBlock(variant) }

    private fun registerPlatform(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_platform") { PlatformBlock(variant) }

    private fun registerStep(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_step") { StepBlock(variant) }

    private fun registerDrawer(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_drawer") { DrawerBlock(variant) }

    private fun registerChair(variant: BlockVariant): Registered<Block> {
        val block = registerBlockWithoutItem("${variant.name}_chair") { ChairBlock(variant) }
        registerItem("${variant.name}_chair") { ChairBlockItem(block.get()) }
        return block
    }

    private fun registerTable(variant: BlockVariant): Registered<Block> {
        val block = registerBlockWithoutItem("${variant.name}_table") { TableBlock(variant) }
        registerItem("${variant.name}_table") { TableBlockItem(block.get()) }
        return block
    }

    private fun registerKitchenCounter(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_kitchen_counter") { KitchenCounterBlock(variant) }

    private fun registerKitchenCupboard(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_kitchen_cupboard") { KitchenCupboardBlock(variant) }

    private fun registerKitchenSink(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_kitchen_sink") { KitchenSinkBlock(variant) }

    private fun registerShelf(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_shelf") { ShelfBlock(variant) }

    private fun registerCoffeeTable(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_coffee_table") { CoffeeTableBlock(variant) }

    private fun registerBench(variant: BlockVariant): Registered<Block> =
        registerBlock("${variant.name}_bench") { BenchBlock(variant) }
}
