package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.item.TradingStationItem
import juuxel.adorn.lib.AdornSounds
import juuxel.adorn.lib.Registered
import juuxel.adorn.lib.RegistryHelper
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.associateLazily
import juuxel.adorn.util.copySettingsSafely
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.block.TorchBlock
import net.minecraft.block.WallTorchBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.DyeColor

@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
object AdornBlocks : RegistryHelper() {
    val SOFAS: Map<DyeColor, SofaBlock> by DyeColor.values().associateLazily {
        // This is one place where the BlockVariant mapping is kept.
        // I will not write out sixteen sofa registrations.
        registerBlock(it.asString() + "_sofa") { PlatformBridges.blockFactory.createSofa(BlockVariant.wool(it)) }
    }

    // @formatter:off
    /* ktlint-disable max-line-length */
    val OAK_CHAIR: Block by registerBlock("oak_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.OAK) }
    val SPRUCE_CHAIR: Block by registerBlock("spruce_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.SPRUCE) }
    val BIRCH_CHAIR: Block by registerBlock("birch_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.BIRCH) }
    val JUNGLE_CHAIR: Block by registerBlock("jungle_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.JUNGLE) }
    val ACACIA_CHAIR: Block by registerBlock("acacia_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.ACACIA) }
    val DARK_OAK_CHAIR: Block by registerBlock("dark_oak_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_CHAIR: Block by registerBlock("mangrove_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.MANGROVE) }
    val CRIMSON_CHAIR: Block by registerBlock("crimson_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.CRIMSON) }
    val WARPED_CHAIR: Block by registerBlock("warped_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.WARPED) }

    val OAK_TABLE: Block by registerBlock("oak_table", ::TableBlockItem) { TableBlock(BlockVariant.OAK) }
    val SPRUCE_TABLE: Block by registerBlock("spruce_table", ::TableBlockItem) { TableBlock(BlockVariant.SPRUCE) }
    val BIRCH_TABLE: Block by registerBlock("birch_table", ::TableBlockItem) { TableBlock(BlockVariant.BIRCH) }
    val JUNGLE_TABLE: Block by registerBlock("jungle_table", ::TableBlockItem) { TableBlock(BlockVariant.JUNGLE) }
    val ACACIA_TABLE: Block by registerBlock("acacia_table", ::TableBlockItem) { TableBlock(BlockVariant.ACACIA) }
    val DARK_OAK_TABLE: Block by registerBlock("dark_oak_table", ::TableBlockItem) { TableBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_TABLE: Block by registerBlock("mangrove_table", ::TableBlockItem) { TableBlock(BlockVariant.MANGROVE) }
    val CRIMSON_TABLE: Block by registerBlock("crimson_table", ::TableBlockItem) { TableBlock(BlockVariant.CRIMSON) }
    val WARPED_TABLE: Block by registerBlock("warped_table", ::TableBlockItem) { TableBlock(BlockVariant.WARPED) }

    val OAK_DRAWER: Block by registerBlock("oak_drawer") { DrawerBlock(BlockVariant.OAK) }
    val SPRUCE_DRAWER: Block by registerBlock("spruce_drawer") { DrawerBlock(BlockVariant.SPRUCE) }
    val BIRCH_DRAWER: Block by registerBlock("birch_drawer") { DrawerBlock(BlockVariant.BIRCH) }
    val JUNGLE_DRAWER: Block by registerBlock("jungle_drawer") { DrawerBlock(BlockVariant.JUNGLE) }
    val ACACIA_DRAWER: Block by registerBlock("acacia_drawer") { DrawerBlock(BlockVariant.ACACIA) }
    val DARK_OAK_DRAWER: Block by registerBlock("dark_oak_drawer") { DrawerBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_DRAWER: Block by registerBlock("mangrove_drawer") { DrawerBlock(BlockVariant.MANGROVE) }
    val CRIMSON_DRAWER: Block by registerBlock("crimson_drawer") { DrawerBlock(BlockVariant.CRIMSON) }
    val WARPED_DRAWER: Block by registerBlock("warped_drawer") { DrawerBlock(BlockVariant.WARPED) }

    val OAK_KITCHEN_COUNTER: Block by registerBlock("oak_kitchen_counter") { KitchenCounterBlock(BlockVariant.OAK) }
    val SPRUCE_KITCHEN_COUNTER: Block by registerBlock("spruce_kitchen_counter") { KitchenCounterBlock(BlockVariant.SPRUCE) }
    val BIRCH_KITCHEN_COUNTER: Block by registerBlock("birch_kitchen_counter") { KitchenCounterBlock(BlockVariant.BIRCH) }
    val JUNGLE_KITCHEN_COUNTER: Block by registerBlock("jungle_kitchen_counter") { KitchenCounterBlock(BlockVariant.JUNGLE) }
    val ACACIA_KITCHEN_COUNTER: Block by registerBlock("acacia_kitchen_counter") { KitchenCounterBlock(BlockVariant.ACACIA) }
    val DARK_OAK_KITCHEN_COUNTER: Block by registerBlock("dark_oak_kitchen_counter") { KitchenCounterBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_KITCHEN_COUNTER: Block by registerBlock("mangrove_kitchen_counter") { KitchenCounterBlock(BlockVariant.MANGROVE) }
    val CRIMSON_KITCHEN_COUNTER: Block by registerBlock("crimson_kitchen_counter") { KitchenCounterBlock(BlockVariant.CRIMSON) }
    val WARPED_KITCHEN_COUNTER: Block by registerBlock("warped_kitchen_counter") { KitchenCounterBlock(BlockVariant.WARPED) }

    val OAK_KITCHEN_CUPBOARD: Block by registerBlock("oak_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.OAK) }
    val SPRUCE_KITCHEN_CUPBOARD: Block by registerBlock("spruce_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.SPRUCE) }
    val BIRCH_KITCHEN_CUPBOARD: Block by registerBlock("birch_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.BIRCH) }
    val JUNGLE_KITCHEN_CUPBOARD: Block by registerBlock("jungle_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.JUNGLE) }
    val ACACIA_KITCHEN_CUPBOARD: Block by registerBlock("acacia_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.ACACIA) }
    val DARK_OAK_KITCHEN_CUPBOARD: Block by registerBlock("dark_oak_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_KITCHEN_CUPBOARD: Block by registerBlock("mangrove_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.MANGROVE) }
    val CRIMSON_KITCHEN_CUPBOARD: Block by registerBlock("crimson_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.CRIMSON) }
    val WARPED_KITCHEN_CUPBOARD: Block by registerBlock("warped_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.WARPED) }

    val OAK_KITCHEN_SINK: Block by registerBlock("oak_kitchen_sink") { KitchenSinkBlock(BlockVariant.OAK) }
    val SPRUCE_KITCHEN_SINK: Block by registerBlock("spruce_kitchen_sink") { KitchenSinkBlock(BlockVariant.SPRUCE) }
    val BIRCH_KITCHEN_SINK: Block by registerBlock("birch_kitchen_sink") { KitchenSinkBlock(BlockVariant.BIRCH) }
    val JUNGLE_KITCHEN_SINK: Block by registerBlock("jungle_kitchen_sink") { KitchenSinkBlock(BlockVariant.JUNGLE) }
    val ACACIA_KITCHEN_SINK: Block by registerBlock("acacia_kitchen_sink") { KitchenSinkBlock(BlockVariant.ACACIA) }
    val DARK_OAK_KITCHEN_SINK: Block by registerBlock("dark_oak_kitchen_sink") { KitchenSinkBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_KITCHEN_SINK: Block by registerBlock("mangrove_kitchen_sink") { KitchenSinkBlock(BlockVariant.MANGROVE) }
    val CRIMSON_KITCHEN_SINK: Block by registerBlock("crimson_kitchen_sink") { KitchenSinkBlock(BlockVariant.CRIMSON) }
    val WARPED_KITCHEN_SINK: Block by registerBlock("warped_kitchen_sink") { KitchenSinkBlock(BlockVariant.WARPED) }

    val OAK_POST: Block by registerBlock("oak_post") { PostBlock(BlockVariant.OAK) }
    val SPRUCE_POST: Block by registerBlock("spruce_post") { PostBlock(BlockVariant.SPRUCE) }
    val BIRCH_POST: Block by registerBlock("birch_post") { PostBlock(BlockVariant.BIRCH) }
    val JUNGLE_POST: Block by registerBlock("jungle_post") { PostBlock(BlockVariant.JUNGLE) }
    val ACACIA_POST: Block by registerBlock("acacia_post") { PostBlock(BlockVariant.ACACIA) }
    val DARK_OAK_POST: Block by registerBlock("dark_oak_post") { PostBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_POST: Block by registerBlock("mangrove_post") { PostBlock(BlockVariant.MANGROVE) }
    val CRIMSON_POST: Block by registerBlock("crimson_post") { PostBlock(BlockVariant.CRIMSON) }
    val WARPED_POST: Block by registerBlock("warped_post") { PostBlock(BlockVariant.WARPED) }
    val STONE_POST: Block by registerBlock("stone_post") { PostBlock(BlockVariant.STONE) }
    val COBBLESTONE_POST: Block by registerBlock("cobblestone_post") { PostBlock(BlockVariant.COBBLESTONE) }
    val SANDSTONE_POST: Block by registerBlock("sandstone_post") { PostBlock(BlockVariant.SANDSTONE) }
    val DIORITE_POST: Block by registerBlock("diorite_post") { PostBlock(BlockVariant.DIORITE) }
    val ANDESITE_POST: Block by registerBlock("andesite_post") { PostBlock(BlockVariant.ANDESITE) }
    val GRANITE_POST: Block by registerBlock("granite_post") { PostBlock(BlockVariant.GRANITE) }
    val BRICK_POST: Block by registerBlock("brick_post") { PostBlock(BlockVariant.BRICK) }
    val STONE_BRICK_POST: Block by registerBlock("stone_brick_post") { PostBlock(BlockVariant.STONE_BRICK) }
    val RED_SANDSTONE_POST: Block by registerBlock("red_sandstone_post") { PostBlock(BlockVariant.RED_SANDSTONE) }
    val NETHER_BRICK_POST: Block by registerBlock("nether_brick_post") { PostBlock(BlockVariant.NETHER_BRICK) }
    val BASALT_POST: Block by registerBlock("basalt_post") { PostBlock(BlockVariant.BASALT) }
    val BLACKSTONE_POST: Block by registerBlock("blackstone_post") { PostBlock(BlockVariant.BLACKSTONE) }
    val RED_NETHER_BRICK_POST: Block by registerBlock("red_nether_brick_post") { PostBlock(BlockVariant.RED_NETHER_BRICK) }
    val PRISMARINE_POST: Block by registerBlock("prismarine_post") { PostBlock(BlockVariant.PRISMARINE) }
    val QUARTZ_POST: Block by registerBlock("quartz_post") { PostBlock(BlockVariant.QUARTZ) }
    val END_STONE_BRICK_POST: Block by registerBlock("end_stone_brick_post") { PostBlock(BlockVariant.END_STONE_BRICK) }
    val PURPUR_POST: Block by registerBlock("purpur_post") { PostBlock(BlockVariant.PURPUR) }
    val POLISHED_BLACKSTONE_POST: Block by registerBlock("polished_blackstone_post") { PostBlock(BlockVariant.POLISHED_BLACKSTONE) }
    val POLISHED_BLACKSTONE_BRICK_POST: Block by registerBlock("polished_blackstone_brick_post") { PostBlock(BlockVariant.POLISHED_BLACKSTONE_BRICK) }
    val POLISHED_DIORITE_POST: Block by registerBlock("polished_diorite_post") { PostBlock(BlockVariant.POLISHED_DIORITE) }
    val POLISHED_ANDESITE_POST: Block by registerBlock("polished_andesite_post") { PostBlock(BlockVariant.POLISHED_ANDESITE) }
    val POLISHED_GRANITE_POST: Block by registerBlock("polished_granite_post") { PostBlock(BlockVariant.POLISHED_GRANITE) }
    val PRISMARINE_BRICK_POST: Block by registerBlock("prismarine_brick_post") { PostBlock(BlockVariant.PRISMARINE_BRICK) }
    val DARK_PRISMARINE_POST: Block by registerBlock("dark_prismarine_post") { PostBlock(BlockVariant.DARK_PRISMARINE) }
    val CUT_SANDSTONE_POST: Block by registerBlock("cut_sandstone_post") { PostBlock(BlockVariant.CUT_SANDSTONE) }
    val SMOOTH_SANDSTONE_POST: Block by registerBlock("smooth_sandstone_post") { PostBlock(BlockVariant.SMOOTH_SANDSTONE) }
    val CUT_RED_SANDSTONE_POST: Block by registerBlock("cut_red_sandstone_post") { PostBlock(BlockVariant.CUT_RED_SANDSTONE) }
    val SMOOTH_RED_SANDSTONE_POST: Block by registerBlock("smooth_red_sandstone_post") { PostBlock(BlockVariant.SMOOTH_RED_SANDSTONE) }
    val SMOOTH_STONE_POST: Block by registerBlock("smooth_stone_post") { PostBlock(BlockVariant.SMOOTH_STONE) }
    val MOSSY_COBBLESTONE_POST: Block by registerBlock("mossy_cobblestone_post") { PostBlock(BlockVariant.MOSSY_COBBLESTONE) }
    val MOSSY_STONE_BRICK_POST: Block by registerBlock("mossy_stone_brick_post") { PostBlock(BlockVariant.MOSSY_STONE_BRICK) }
    val DEEPSLATE_POST: Block by registerBlock("deepslate_post") { PostBlock(BlockVariant.DEEPSLATE) }
    val COBBLED_DEEPSLATE_POST: Block by registerBlock("cobbled_deepslate_post") { PostBlock(BlockVariant.COBBLED_DEEPSLATE) }
    val POLISHED_DEEPSLATE_POST: Block by registerBlock("polished_deepslate_post") { PostBlock(BlockVariant.POLISHED_DEEPSLATE) }
    val DEEPSLATE_BRICK_POST: Block by registerBlock("deepslate_brick_post") { PostBlock(BlockVariant.DEEPSLATE_BRICK) }
    val DEEPSLATE_TILE_POST: Block by registerBlock("deepslate_tile_post") { PostBlock(BlockVariant.DEEPSLATE_TILE) }

    val OAK_PLATFORM: Block by registerBlock("oak_platform") { PlatformBlock(BlockVariant.OAK) }
    val SPRUCE_PLATFORM: Block by registerBlock("spruce_platform") { PlatformBlock(BlockVariant.SPRUCE) }
    val BIRCH_PLATFORM: Block by registerBlock("birch_platform") { PlatformBlock(BlockVariant.BIRCH) }
    val JUNGLE_PLATFORM: Block by registerBlock("jungle_platform") { PlatformBlock(BlockVariant.JUNGLE) }
    val ACACIA_PLATFORM: Block by registerBlock("acacia_platform") { PlatformBlock(BlockVariant.ACACIA) }
    val DARK_OAK_PLATFORM: Block by registerBlock("dark_oak_platform") { PlatformBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_PLATFORM: Block by registerBlock("mangrove_platform") { PlatformBlock(BlockVariant.MANGROVE) }
    val CRIMSON_PLATFORM: Block by registerBlock("crimson_platform") { PlatformBlock(BlockVariant.CRIMSON) }
    val WARPED_PLATFORM: Block by registerBlock("warped_platform") { PlatformBlock(BlockVariant.WARPED) }
    val STONE_PLATFORM: Block by registerBlock("stone_platform") { PlatformBlock(BlockVariant.STONE) }
    val COBBLESTONE_PLATFORM: Block by registerBlock("cobblestone_platform") { PlatformBlock(BlockVariant.COBBLESTONE) }
    val SANDSTONE_PLATFORM: Block by registerBlock("sandstone_platform") { PlatformBlock(BlockVariant.SANDSTONE) }
    val DIORITE_PLATFORM: Block by registerBlock("diorite_platform") { PlatformBlock(BlockVariant.DIORITE) }
    val ANDESITE_PLATFORM: Block by registerBlock("andesite_platform") { PlatformBlock(BlockVariant.ANDESITE) }
    val GRANITE_PLATFORM: Block by registerBlock("granite_platform") { PlatformBlock(BlockVariant.GRANITE) }
    val BRICK_PLATFORM: Block by registerBlock("brick_platform") { PlatformBlock(BlockVariant.BRICK) }
    val STONE_BRICK_PLATFORM: Block by registerBlock("stone_brick_platform") { PlatformBlock(BlockVariant.STONE_BRICK) }
    val RED_SANDSTONE_PLATFORM: Block by registerBlock("red_sandstone_platform") { PlatformBlock(BlockVariant.RED_SANDSTONE) }
    val NETHER_BRICK_PLATFORM: Block by registerBlock("nether_brick_platform") { PlatformBlock(BlockVariant.NETHER_BRICK) }
    val BASALT_PLATFORM: Block by registerBlock("basalt_platform") { PlatformBlock(BlockVariant.BASALT) }
    val BLACKSTONE_PLATFORM: Block by registerBlock("blackstone_platform") { PlatformBlock(BlockVariant.BLACKSTONE) }
    val RED_NETHER_BRICK_PLATFORM: Block by registerBlock("red_nether_brick_platform") { PlatformBlock(BlockVariant.RED_NETHER_BRICK) }
    val PRISMARINE_PLATFORM: Block by registerBlock("prismarine_platform") { PlatformBlock(BlockVariant.PRISMARINE) }
    val QUARTZ_PLATFORM: Block by registerBlock("quartz_platform") { PlatformBlock(BlockVariant.QUARTZ) }
    val END_STONE_BRICK_PLATFORM: Block by registerBlock("end_stone_brick_platform") { PlatformBlock(BlockVariant.END_STONE_BRICK) }
    val PURPUR_PLATFORM: Block by registerBlock("purpur_platform") { PlatformBlock(BlockVariant.PURPUR) }
    val POLISHED_BLACKSTONE_PLATFORM: Block by registerBlock("polished_blackstone_platform") { PlatformBlock(BlockVariant.POLISHED_BLACKSTONE) }
    val POLISHED_BLACKSTONE_BRICK_PLATFORM: Block by registerBlock("polished_blackstone_brick_platform") { PlatformBlock(BlockVariant.POLISHED_BLACKSTONE_BRICK) }
    val POLISHED_DIORITE_PLATFORM: Block by registerBlock("polished_diorite_platform") { PlatformBlock(BlockVariant.POLISHED_DIORITE) }
    val POLISHED_ANDESITE_PLATFORM: Block by registerBlock("polished_andesite_platform") { PlatformBlock(BlockVariant.POLISHED_ANDESITE) }
    val POLISHED_GRANITE_PLATFORM: Block by registerBlock("polished_granite_platform") { PlatformBlock(BlockVariant.POLISHED_GRANITE) }
    val PRISMARINE_BRICK_PLATFORM: Block by registerBlock("prismarine_brick_platform") { PlatformBlock(BlockVariant.PRISMARINE_BRICK) }
    val DARK_PRISMARINE_PLATFORM: Block by registerBlock("dark_prismarine_platform") { PlatformBlock(BlockVariant.DARK_PRISMARINE) }
    val CUT_SANDSTONE_PLATFORM: Block by registerBlock("cut_sandstone_platform") { PlatformBlock(BlockVariant.CUT_SANDSTONE) }
    val SMOOTH_SANDSTONE_PLATFORM: Block by registerBlock("smooth_sandstone_platform") { PlatformBlock(BlockVariant.SMOOTH_SANDSTONE) }
    val CUT_RED_SANDSTONE_PLATFORM: Block by registerBlock("cut_red_sandstone_platform") { PlatformBlock(BlockVariant.CUT_RED_SANDSTONE) }
    val SMOOTH_RED_SANDSTONE_PLATFORM: Block by registerBlock("smooth_red_sandstone_platform") { PlatformBlock(BlockVariant.SMOOTH_RED_SANDSTONE) }
    val SMOOTH_STONE_PLATFORM: Block by registerBlock("smooth_stone_platform") { PlatformBlock(BlockVariant.SMOOTH_STONE) }
    val MOSSY_COBBLESTONE_PLATFORM: Block by registerBlock("mossy_cobblestone_platform") { PlatformBlock(BlockVariant.MOSSY_COBBLESTONE) }
    val MOSSY_STONE_BRICK_PLATFORM: Block by registerBlock("mossy_stone_brick_platform") { PlatformBlock(BlockVariant.MOSSY_STONE_BRICK) }
    val DEEPSLATE_PLATFORM: Block by registerBlock("deepslate_platform") { PlatformBlock(BlockVariant.DEEPSLATE) }
    val COBBLED_DEEPSLATE_PLATFORM: Block by registerBlock("cobbled_deepslate_platform") { PlatformBlock(BlockVariant.COBBLED_DEEPSLATE) }
    val POLISHED_DEEPSLATE_PLATFORM: Block by registerBlock("polished_deepslate_platform") { PlatformBlock(BlockVariant.POLISHED_DEEPSLATE) }
    val DEEPSLATE_BRICK_PLATFORM: Block by registerBlock("deepslate_brick_platform") { PlatformBlock(BlockVariant.DEEPSLATE_BRICK) }
    val DEEPSLATE_TILE_PLATFORM: Block by registerBlock("deepslate_tile_platform") { PlatformBlock(BlockVariant.DEEPSLATE_TILE) }

    val OAK_STEP: Block by registerBlock("oak_step") { StepBlock(BlockVariant.OAK) }
    val SPRUCE_STEP: Block by registerBlock("spruce_step") { StepBlock(BlockVariant.SPRUCE) }
    val BIRCH_STEP: Block by registerBlock("birch_step") { StepBlock(BlockVariant.BIRCH) }
    val JUNGLE_STEP: Block by registerBlock("jungle_step") { StepBlock(BlockVariant.JUNGLE) }
    val ACACIA_STEP: Block by registerBlock("acacia_step") { StepBlock(BlockVariant.ACACIA) }
    val DARK_OAK_STEP: Block by registerBlock("dark_oak_step") { StepBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_STEP: Block by registerBlock("mangrove_step") { StepBlock(BlockVariant.MANGROVE) }
    val CRIMSON_STEP: Block by registerBlock("crimson_step") { StepBlock(BlockVariant.CRIMSON) }
    val WARPED_STEP: Block by registerBlock("warped_step") { StepBlock(BlockVariant.WARPED) }
    val STONE_STEP: Block by registerBlock("stone_step") { StepBlock(BlockVariant.STONE) }
    val COBBLESTONE_STEP: Block by registerBlock("cobblestone_step") { StepBlock(BlockVariant.COBBLESTONE) }
    val SANDSTONE_STEP: Block by registerBlock("sandstone_step") { StepBlock(BlockVariant.SANDSTONE) }
    val DIORITE_STEP: Block by registerBlock("diorite_step") { StepBlock(BlockVariant.DIORITE) }
    val ANDESITE_STEP: Block by registerBlock("andesite_step") { StepBlock(BlockVariant.ANDESITE) }
    val GRANITE_STEP: Block by registerBlock("granite_step") { StepBlock(BlockVariant.GRANITE) }
    val BRICK_STEP: Block by registerBlock("brick_step") { StepBlock(BlockVariant.BRICK) }
    val STONE_BRICK_STEP: Block by registerBlock("stone_brick_step") { StepBlock(BlockVariant.STONE_BRICK) }
    val RED_SANDSTONE_STEP: Block by registerBlock("red_sandstone_step") { StepBlock(BlockVariant.RED_SANDSTONE) }
    val NETHER_BRICK_STEP: Block by registerBlock("nether_brick_step") { StepBlock(BlockVariant.NETHER_BRICK) }
    val BASALT_STEP: Block by registerBlock("basalt_step") { StepBlock(BlockVariant.BASALT) }
    val BLACKSTONE_STEP: Block by registerBlock("blackstone_step") { StepBlock(BlockVariant.BLACKSTONE) }
    val RED_NETHER_BRICK_STEP: Block by registerBlock("red_nether_brick_step") { StepBlock(BlockVariant.RED_NETHER_BRICK) }
    val PRISMARINE_STEP: Block by registerBlock("prismarine_step") { StepBlock(BlockVariant.PRISMARINE) }
    val QUARTZ_STEP: Block by registerBlock("quartz_step") { StepBlock(BlockVariant.QUARTZ) }
    val END_STONE_BRICK_STEP: Block by registerBlock("end_stone_brick_step") { StepBlock(BlockVariant.END_STONE_BRICK) }
    val PURPUR_STEP: Block by registerBlock("purpur_step") { StepBlock(BlockVariant.PURPUR) }
    val POLISHED_BLACKSTONE_STEP: Block by registerBlock("polished_blackstone_step") { StepBlock(BlockVariant.POLISHED_BLACKSTONE) }
    val POLISHED_BLACKSTONE_BRICK_STEP: Block by registerBlock("polished_blackstone_brick_step") { StepBlock(BlockVariant.POLISHED_BLACKSTONE_BRICK) }
    val POLISHED_DIORITE_STEP: Block by registerBlock("polished_diorite_step") { StepBlock(BlockVariant.POLISHED_DIORITE) }
    val POLISHED_ANDESITE_STEP: Block by registerBlock("polished_andesite_step") { StepBlock(BlockVariant.POLISHED_ANDESITE) }
    val POLISHED_GRANITE_STEP: Block by registerBlock("polished_granite_step") { StepBlock(BlockVariant.POLISHED_GRANITE) }
    val PRISMARINE_BRICK_STEP: Block by registerBlock("prismarine_brick_step") { StepBlock(BlockVariant.PRISMARINE_BRICK) }
    val DARK_PRISMARINE_STEP: Block by registerBlock("dark_prismarine_step") { StepBlock(BlockVariant.DARK_PRISMARINE) }
    val CUT_SANDSTONE_STEP: Block by registerBlock("cut_sandstone_step") { StepBlock(BlockVariant.CUT_SANDSTONE) }
    val SMOOTH_SANDSTONE_STEP: Block by registerBlock("smooth_sandstone_step") { StepBlock(BlockVariant.SMOOTH_SANDSTONE) }
    val CUT_RED_SANDSTONE_STEP: Block by registerBlock("cut_red_sandstone_step") { StepBlock(BlockVariant.CUT_RED_SANDSTONE) }
    val SMOOTH_RED_SANDSTONE_STEP: Block by registerBlock("smooth_red_sandstone_step") { StepBlock(BlockVariant.SMOOTH_RED_SANDSTONE) }
    val SMOOTH_STONE_STEP: Block by registerBlock("smooth_stone_step") { StepBlock(BlockVariant.SMOOTH_STONE) }
    val MOSSY_COBBLESTONE_STEP: Block by registerBlock("mossy_cobblestone_step") { StepBlock(BlockVariant.MOSSY_COBBLESTONE) }
    val MOSSY_STONE_BRICK_STEP: Block by registerBlock("mossy_stone_brick_step") { StepBlock(BlockVariant.MOSSY_STONE_BRICK) }
    val DEEPSLATE_STEP: Block by registerBlock("deepslate_step") { StepBlock(BlockVariant.DEEPSLATE) }
    val COBBLED_DEEPSLATE_STEP: Block by registerBlock("cobbled_deepslate_step") { StepBlock(BlockVariant.COBBLED_DEEPSLATE) }
    val POLISHED_DEEPSLATE_STEP: Block by registerBlock("polished_deepslate_step") { StepBlock(BlockVariant.POLISHED_DEEPSLATE) }
    val DEEPSLATE_BRICK_STEP: Block by registerBlock("deepslate_brick_step") { StepBlock(BlockVariant.DEEPSLATE_BRICK) }
    val DEEPSLATE_TILE_STEP: Block by registerBlock("deepslate_tile_step") { StepBlock(BlockVariant.DEEPSLATE_TILE) }

    val OAK_SHELF: Block by registerBlock("oak_shelf") { ShelfBlock(BlockVariant.OAK) }
    val SPRUCE_SHELF: Block by registerBlock("spruce_shelf") { ShelfBlock(BlockVariant.SPRUCE) }
    val BIRCH_SHELF: Block by registerBlock("birch_shelf") { ShelfBlock(BlockVariant.BIRCH) }
    val JUNGLE_SHELF: Block by registerBlock("jungle_shelf") { ShelfBlock(BlockVariant.JUNGLE) }
    val ACACIA_SHELF: Block by registerBlock("acacia_shelf") { ShelfBlock(BlockVariant.ACACIA) }
    val DARK_OAK_SHELF: Block by registerBlock("dark_oak_shelf") { ShelfBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_SHELF: Block by registerBlock("mangrove_shelf") { ShelfBlock(BlockVariant.MANGROVE) }
    val CRIMSON_SHELF: Block by registerBlock("crimson_shelf") { ShelfBlock(BlockVariant.CRIMSON) }
    val WARPED_SHELF: Block by registerBlock("warped_shelf") { ShelfBlock(BlockVariant.WARPED) }
    val IRON_SHELF: Block by registerBlock("iron_shelf") { ShelfBlock(BlockVariant.IRON) }

    val BRICK_CHIMNEY: Block by registerBlock("brick_chimney") {
        ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.RED))
    }
    val STONE_BRICK_CHIMNEY: Block by registerBlock("stone_brick_chimney") {
        ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.STONE_GRAY))
    }
    val NETHER_BRICK_CHIMNEY: Block by registerBlock("nether_brick_chimney") {
        ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.DARK_RED))
    }
    val RED_NETHER_BRICK_CHIMNEY: Block by registerBlock("red_nether_brick_chimney") {
        ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.DARK_RED))
    }
    val COBBLESTONE_CHIMNEY: Block by registerBlock("cobblestone_chimney") {
        ChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.STONE_GRAY))
    }
    val PRISMARINE_CHIMNEY: Block by registerBlock("prismarine_chimney") {
        PrismarineChimneyBlock(AbstractChimneyBlock.createBlockSettings(MapColor.CYAN, hardness = 1.5f))
    }
    val MAGMATIC_PRISMARINE_CHIMNEY: Block by registerBlock("magmatic_prismarine_chimney") {
        PrismarineChimneyBlock.WithColumn(true, AbstractChimneyBlock.createBlockSettings(MapColor.CYAN, hardness = 1.5f).luminance { 3 })
    }
    val SOULFUL_PRISMARINE_CHIMNEY: Block by registerBlock("soulful_prismarine_chimney") {
        PrismarineChimneyBlock.WithColumn(false, AbstractChimneyBlock.createBlockSettings(MapColor.CYAN, hardness = 1.5f))
    }

    val OAK_COFFEE_TABLE: Block by registerBlock("oak_coffee_table") { CoffeeTableBlock(BlockVariant.OAK) }
    val SPRUCE_COFFEE_TABLE: Block by registerBlock("spruce_coffee_table") { CoffeeTableBlock(BlockVariant.SPRUCE) }
    val BIRCH_COFFEE_TABLE: Block by registerBlock("birch_coffee_table") { CoffeeTableBlock(BlockVariant.BIRCH) }
    val JUNGLE_COFFEE_TABLE: Block by registerBlock("jungle_coffee_table") { CoffeeTableBlock(BlockVariant.JUNGLE) }
    val ACACIA_COFFEE_TABLE: Block by registerBlock("acacia_coffee_table") { CoffeeTableBlock(BlockVariant.ACACIA) }
    val DARK_OAK_COFFEE_TABLE: Block by registerBlock("dark_oak_coffee_table") { CoffeeTableBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_COFFEE_TABLE: Block by registerBlock("mangrove_coffee_table") { CoffeeTableBlock(BlockVariant.MANGROVE) }
    val CRIMSON_COFFEE_TABLE: Block by registerBlock("crimson_coffee_table") { CoffeeTableBlock(BlockVariant.CRIMSON) }
    val WARPED_COFFEE_TABLE: Block by registerBlock("warped_coffee_table") { CoffeeTableBlock(BlockVariant.WARPED) }

    val OAK_BENCH: Block by registerBlock("oak_bench") { BenchBlock(BlockVariant.OAK) }
    val SPRUCE_BENCH: Block by registerBlock("spruce_bench") { BenchBlock(BlockVariant.SPRUCE) }
    val BIRCH_BENCH: Block by registerBlock("birch_bench") { BenchBlock(BlockVariant.BIRCH) }
    val JUNGLE_BENCH: Block by registerBlock("jungle_bench") { BenchBlock(BlockVariant.JUNGLE) }
    val ACACIA_BENCH: Block by registerBlock("acacia_bench") { BenchBlock(BlockVariant.ACACIA) }
    val DARK_OAK_BENCH: Block by registerBlock("dark_oak_bench") { BenchBlock(BlockVariant.DARK_OAK) }
    val MANGROVE_BENCH: Block by registerBlock("mangrove_bench") { BenchBlock(BlockVariant.MANGROVE) }
    val CRIMSON_BENCH: Block by registerBlock("crimson_bench") { BenchBlock(BlockVariant.CRIMSON) }
    val WARPED_BENCH: Block by registerBlock("warped_bench") { BenchBlock(BlockVariant.WARPED) }

    val TABLE_LAMPS: Map<DyeColor, Block> by DyeColor.values().associateLazily {
        registerBlock("${it.asString()}_table_lamp") {
            TableLampBlock(TableLampBlock.createBlockSettings(it))
        }
    }

    val TRADING_STATION: Block by registerBlock(
        "trading_station",
        itemProvider = { TradingStationItem(it, Item.Settings().group(ItemGroup.DECORATIONS)) },
        block = { TradingStationBlock(AbstractBlock.Settings.of(Material.WOOD).strength(2.5f).sounds(BlockSoundGroup.WOOD)) }
    )

    val STONE_TORCH_GROUND: Block by registerBlockWithoutItem("stone_torch") {
        TorchBlock(
            AbstractBlock.Settings.copy(Blocks.TORCH)
                .sounds(BlockSoundGroup.STONE)
                .luminance { 15 },
            ParticleTypes.FLAME
        )
    }
    val STONE_TORCH_WALL: Block by registerBlockWithoutItem("wall_stone_torch") {
        WallTorchBlock(
            AbstractBlock.Settings.copy(STONE_TORCH_GROUND).dropsLike(STONE_TORCH_GROUND),
            ParticleTypes.FLAME
        )
    }

    val CRATE: Block by registerBlock("crate") { Block(Blocks.OAK_PLANKS.copySettingsSafely()) }
    val APPLE_CRATE: Block by registerCrate("apple_crate")
    val WHEAT_CRATE: Block by registerCrate("wheat_crate")
    val CARROT_CRATE: Block by registerCrate("carrot_crate")
    val POTATO_CRATE: Block by registerCrate("potato_crate")
    val MELON_CRATE: Block by registerCrate("melon_crate")
    val WHEAT_SEED_CRATE: Block by registerCrate("wheat_seed_crate")
    val MELON_SEED_CRATE: Block by registerCrate("melon_seed_crate")
    val PUMPKIN_SEED_CRATE: Block by registerCrate("pumpkin_seed_crate")
    val BEETROOT_CRATE: Block by registerCrate("beetroot_crate")
    val BEETROOT_SEED_CRATE: Block by registerCrate("beetroot_seed_crate")
    val SWEET_BERRY_CRATE: Block by registerCrate("sweet_berry_crate")
    val COCOA_BEAN_CRATE: Block by registerCrate("cocoa_bean_crate")
    val NETHER_WART_CRATE: Block by registerCrate("nether_wart_crate")
    val SUGAR_CANE_CRATE: Block by registerCrate("sugar_cane_crate")
    val EGG_CRATE: Block by registerCrate("egg_crate")
    val HONEYCOMB_CRATE: Block by registerCrate("honeycomb_crate")
    val LIL_TATER_CRATE: Block by registerCrate("lil_tater_crate")

    val PICKET_FENCE: Block by registerBlock("picket_fence") {
        PicketFenceBlock(AbstractBlock.Settings.copy(Blocks.OAK_FENCE).nonOpaque())
    }
    val CHAIN_LINK_FENCE: Block by registerBlock("chain_link_fence") {
        ChainLinkFenceBlock(
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                .sounds(AdornSounds.CHAIN_LINK_FENCE)
        )
    }
    val STONE_LADDER: Block by registerBlock("stone_ladder") {
        StoneLadderBlock(
            AbstractBlock.Settings.copy(Blocks.STONE)
                .nonOpaque()
        )
    }
    val BREWER: Block by registerBlock("brewer") { BrewerBlock(AbstractBlock.Settings.of(Material.METAL).strength(0.8F)) }
    /* ktlint-enable max-line-length */
    // @formatter:on

    fun init() {
    }

    private fun registerCrate(name: String): Registered<Block> =
        registerBlock(
            name,
            itemSettings = { Item.Settings().group(ItemGroup.DECORATIONS).recipeRemainder(CRATE.asItem()) },
            block = { Block(CRATE.copySettingsSafely()) }
        )
}
