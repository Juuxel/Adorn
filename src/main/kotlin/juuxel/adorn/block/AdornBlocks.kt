package juuxel.adorn.block

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.client.SinkColorProvider
import juuxel.adorn.client.renderer.ShelfRenderer
import juuxel.adorn.client.renderer.TradingStationRenderer
import juuxel.adorn.item.BaseBlockItem
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.lib.AdornSounds
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.CarpetBlock
import net.minecraft.block.TorchBlock
import net.minecraft.block.WallTorchBlock
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.common.ToolType
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

private typealias BlockDef = RegistryObject<Block>

@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
object AdornBlocks {
    val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Adorn.NAMESPACE)
    val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Adorn.NAMESPACE)

    val SOFAS: Map<DyeColor, BlockDef> = DyeColor.values().associate {
        // This is one place where the BlockVariant mapping is kept.
        // I will not write out sixteen sofa registrations.
        it to registerBlock(it.asString() + "_sofa") { SofaBlock(BlockVariant.wool(it)) }
    }

    // @formatter:off
    val OAK_CHAIR: BlockDef = registerBlock("oak_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.OAK) }
    val SPRUCE_CHAIR: BlockDef = registerBlock("spruce_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.SPRUCE) }
    val BIRCH_CHAIR: BlockDef = registerBlock("birch_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.BIRCH) }
    val JUNGLE_CHAIR: BlockDef = registerBlock("jungle_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.JUNGLE) }
    val ACACIA_CHAIR: BlockDef = registerBlock("acacia_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.ACACIA) }
    val DARK_OAK_CHAIR: BlockDef = registerBlock("dark_oak_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_CHAIR: BlockDef = registerBlock("crimson_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.CRIMSON) }
    val WARPED_CHAIR: BlockDef = registerBlock("warped_chair", ::ChairBlockItem) { ChairBlock(BlockVariant.WARPED) }

    val OAK_TABLE: BlockDef = registerBlock("oak_table", ::TableBlockItem) { TableBlock(BlockVariant.OAK) }
    val SPRUCE_TABLE: BlockDef = registerBlock("spruce_table", ::TableBlockItem) { TableBlock(BlockVariant.SPRUCE) }
    val BIRCH_TABLE: BlockDef = registerBlock("birch_table", ::TableBlockItem) { TableBlock(BlockVariant.BIRCH) }
    val JUNGLE_TABLE: BlockDef = registerBlock("jungle_table", ::TableBlockItem) { TableBlock(BlockVariant.JUNGLE) }
    val ACACIA_TABLE: BlockDef = registerBlock("acacia_table", ::TableBlockItem) { TableBlock(BlockVariant.ACACIA) }
    val DARK_OAK_TABLE: BlockDef = registerBlock("dark_oak_table", ::TableBlockItem) { TableBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_TABLE: BlockDef = registerBlock("crimson_table", ::TableBlockItem) { TableBlock(BlockVariant.CRIMSON) }
    val WARPED_TABLE: BlockDef = registerBlock("warped_table", ::TableBlockItem) { TableBlock(BlockVariant.WARPED) }

    val OAK_KITCHEN_COUNTER: BlockDef = registerBlock("oak_kitchen_counter") { KitchenCounterBlock(BlockVariant.OAK) }
    val SPRUCE_KITCHEN_COUNTER: BlockDef = registerBlock("spruce_kitchen_counter") { KitchenCounterBlock(BlockVariant.SPRUCE) }
    val BIRCH_KITCHEN_COUNTER: BlockDef = registerBlock("birch_kitchen_counter") { KitchenCounterBlock(BlockVariant.BIRCH) }
    val JUNGLE_KITCHEN_COUNTER: BlockDef = registerBlock("jungle_kitchen_counter") { KitchenCounterBlock(BlockVariant.JUNGLE) }
    val ACACIA_KITCHEN_COUNTER: BlockDef = registerBlock("acacia_kitchen_counter") { KitchenCounterBlock(BlockVariant.ACACIA) }
    val DARK_OAK_KITCHEN_COUNTER: BlockDef = registerBlock("dark_oak_kitchen_counter") { KitchenCounterBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_KITCHEN_COUNTER: BlockDef = registerBlock("crimson_kitchen_counter") { KitchenCounterBlock(BlockVariant.CRIMSON) }
    val WARPED_KITCHEN_COUNTER: BlockDef = registerBlock("warped_kitchen_counter") { KitchenCounterBlock(BlockVariant.WARPED) }

    val OAK_KITCHEN_CUPBOARD: BlockDef = registerBlock("oak_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.OAK) }
    val SPRUCE_KITCHEN_CUPBOARD: BlockDef = registerBlock("spruce_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.SPRUCE) }
    val BIRCH_KITCHEN_CUPBOARD: BlockDef = registerBlock("birch_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.BIRCH) }
    val JUNGLE_KITCHEN_CUPBOARD: BlockDef = registerBlock("jungle_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.JUNGLE) }
    val ACACIA_KITCHEN_CUPBOARD: BlockDef = registerBlock("acacia_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.ACACIA) }
    val DARK_OAK_KITCHEN_CUPBOARD: BlockDef = registerBlock("dark_oak_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_KITCHEN_CUPBOARD: BlockDef = registerBlock("crimson_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.CRIMSON) }
    val WARPED_KITCHEN_CUPBOARD: BlockDef = registerBlock("warped_kitchen_cupboard") { KitchenCupboardBlock(BlockVariant.WARPED) }

    val OAK_DRAWER: BlockDef = registerBlock("oak_drawer") { DrawerBlock(BlockVariant.OAK) }
    val SPRUCE_DRAWER: BlockDef = registerBlock("spruce_drawer") { DrawerBlock(BlockVariant.SPRUCE) }
    val BIRCH_DRAWER: BlockDef = registerBlock("birch_drawer") { DrawerBlock(BlockVariant.BIRCH) }
    val JUNGLE_DRAWER: BlockDef = registerBlock("jungle_drawer") { DrawerBlock(BlockVariant.JUNGLE) }
    val ACACIA_DRAWER: BlockDef = registerBlock("acacia_drawer") { DrawerBlock(BlockVariant.ACACIA) }
    val DARK_OAK_DRAWER: BlockDef = registerBlock("dark_oak_drawer") { DrawerBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_DRAWER: BlockDef = registerBlock("crimson_drawer") { DrawerBlock(BlockVariant.CRIMSON) }
    val WARPED_DRAWER: BlockDef = registerBlock("warped_drawer") { DrawerBlock(BlockVariant.WARPED) }

    val OAK_POST: BlockDef = registerBlock("oak_post") { PostBlock(BlockVariant.OAK) }
    val SPRUCE_POST: BlockDef = registerBlock("spruce_post") { PostBlock(BlockVariant.SPRUCE) }
    val BIRCH_POST: BlockDef = registerBlock("birch_post") { PostBlock(BlockVariant.BIRCH) }
    val JUNGLE_POST: BlockDef = registerBlock("jungle_post") { PostBlock(BlockVariant.JUNGLE) }
    val ACACIA_POST: BlockDef = registerBlock("acacia_post") { PostBlock(BlockVariant.ACACIA) }
    val DARK_OAK_POST: BlockDef = registerBlock("dark_oak_post") { PostBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_POST: BlockDef = registerBlock("crimson_post") { PostBlock(BlockVariant.CRIMSON) }
    val WARPED_POST: BlockDef = registerBlock("warped_post") { PostBlock(BlockVariant.WARPED) }
    val STONE_POST: BlockDef = registerBlock("stone_post") { PostBlock(BlockVariant.STONE) }
    val COBBLESTONE_POST: BlockDef = registerBlock("cobblestone_post") { PostBlock(BlockVariant.COBBLESTONE) }
    val SANDSTONE_POST: BlockDef = registerBlock("sandstone_post") { PostBlock(BlockVariant.SANDSTONE) }
    val DIORITE_POST: BlockDef = registerBlock("diorite_post") { PostBlock(BlockVariant.DIORITE) }
    val ANDESITE_POST: BlockDef = registerBlock("andesite_post") { PostBlock(BlockVariant.ANDESITE) }
    val GRANITE_POST: BlockDef = registerBlock("granite_post") { PostBlock(BlockVariant.GRANITE) }
    val BRICK_POST: BlockDef = registerBlock("brick_post") { PostBlock(BlockVariant.BRICK) }
    val STONE_BRICK_POST: BlockDef = registerBlock("stone_brick_post") { PostBlock(BlockVariant.STONE_BRICK) }
    val RED_SANDSTONE_POST: BlockDef = registerBlock("red_sandstone_post") { PostBlock(BlockVariant.RED_SANDSTONE) }
    val NETHER_BRICK_POST: BlockDef = registerBlock("nether_brick_post") { PostBlock(BlockVariant.NETHER_BRICK) }
    val BASALT_POST: BlockDef = registerBlock("basalt_post") { PostBlock(BlockVariant.BASALT) }
    val BLACKSTONE_POST: BlockDef = registerBlock("blackstone_post") { PostBlock(BlockVariant.BLACKSTONE) }
    val RED_NETHER_BRICK_POST: BlockDef = registerBlock("red_nether_brick_post") { PostBlock(BlockVariant.RED_NETHER_BRICK) }
    val PRISMARINE_POST: BlockDef = registerBlock("prismarine_post") { PostBlock(BlockVariant.PRISMARINE) }
    val QUARTZ_POST: BlockDef = registerBlock("quartz_post") { PostBlock(BlockVariant.QUARTZ) }
    val END_STONE_BRICK_POST: BlockDef = registerBlock("end_stone_brick_post") { PostBlock(BlockVariant.END_STONE_BRICK) }
    val PURPUR_POST: BlockDef = registerBlock("purpur_post") { PostBlock(BlockVariant.PURPUR) }
    val POLISHED_BLACKSTONE_POST: BlockDef = registerBlock("polished_blackstone_post") { PostBlock(BlockVariant.POLISHED_BLACKSTONE) }
    val POLISHED_BLACKSTONE_BRICK_POST: BlockDef = registerBlock("polished_blackstone_brick_post") { PostBlock(BlockVariant.POLISHED_BLACKSTONE_BRICK) }
    val POLISHED_DIORITE_POST: BlockDef = registerBlock("polished_diorite_post") { PostBlock(BlockVariant.POLISHED_DIORITE) }
    val POLISHED_ANDESITE_POST: BlockDef = registerBlock("polished_andesite_post") { PostBlock(BlockVariant.POLISHED_ANDESITE) }
    val POLISHED_GRANITE_POST: BlockDef = registerBlock("polished_granite_post") { PostBlock(BlockVariant.POLISHED_GRANITE) }
    val PRISMARINE_BRICK_POST: BlockDef = registerBlock("prismarine_brick_post") { PostBlock(BlockVariant.PRISMARINE_BRICK) }
    val DARK_PRISMARINE_POST: BlockDef = registerBlock("dark_prismarine_post") { PostBlock(BlockVariant.DARK_PRISMARINE) }
    val CUT_SANDSTONE_POST: BlockDef = registerBlock("cut_sandstone_post") { PostBlock(BlockVariant.CUT_SANDSTONE) }
    val SMOOTH_SANDSTONE_POST: BlockDef = registerBlock("smooth_sandstone_post") { PostBlock(BlockVariant.SMOOTH_SANDSTONE) }
    val CUT_RED_SANDSTONE_POST: BlockDef = registerBlock("cut_red_sandstone_post") { PostBlock(BlockVariant.CUT_RED_SANDSTONE) }
    val SMOOTH_RED_SANDSTONE_POST: BlockDef = registerBlock("smooth_red_sandstone_post") { PostBlock(BlockVariant.SMOOTH_RED_SANDSTONE) }
    val SMOOTH_STONE_POST: BlockDef = registerBlock("smooth_stone_post") { PostBlock(BlockVariant.SMOOTH_STONE) }
    val MOSSY_COBBLESTONE_POST: BlockDef = registerBlock("mossy_cobblestone_post") { PostBlock(BlockVariant.MOSSY_COBBLESTONE) }
    val MOSSY_STONE_BRICK_POST: BlockDef = registerBlock("mossy_stone_brick_post") { PostBlock(BlockVariant.MOSSY_STONE_BRICK) }

    val OAK_PLATFORM: BlockDef = registerBlock("oak_platform") { PlatformBlock(BlockVariant.OAK) }
    val SPRUCE_PLATFORM: BlockDef = registerBlock("spruce_platform") { PlatformBlock(BlockVariant.SPRUCE) }
    val BIRCH_PLATFORM: BlockDef = registerBlock("birch_platform") { PlatformBlock(BlockVariant.BIRCH) }
    val JUNGLE_PLATFORM: BlockDef = registerBlock("jungle_platform") { PlatformBlock(BlockVariant.JUNGLE) }
    val ACACIA_PLATFORM: BlockDef = registerBlock("acacia_platform") { PlatformBlock(BlockVariant.ACACIA) }
    val DARK_OAK_PLATFORM: BlockDef = registerBlock("dark_oak_platform") { PlatformBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_PLATFORM: BlockDef = registerBlock("crimson_platform") { PlatformBlock(BlockVariant.CRIMSON) }
    val WARPED_PLATFORM: BlockDef = registerBlock("warped_platform") { PlatformBlock(BlockVariant.WARPED) }
    val STONE_PLATFORM: BlockDef = registerBlock("stone_platform") { PlatformBlock(BlockVariant.STONE) }
    val COBBLESTONE_PLATFORM: BlockDef = registerBlock("cobblestone_platform") { PlatformBlock(BlockVariant.COBBLESTONE) }
    val SANDSTONE_PLATFORM: BlockDef = registerBlock("sandstone_platform") { PlatformBlock(BlockVariant.SANDSTONE) }
    val DIORITE_PLATFORM: BlockDef = registerBlock("diorite_platform") { PlatformBlock(BlockVariant.DIORITE) }
    val ANDESITE_PLATFORM: BlockDef = registerBlock("andesite_platform") { PlatformBlock(BlockVariant.ANDESITE) }
    val GRANITE_PLATFORM: BlockDef = registerBlock("granite_platform") { PlatformBlock(BlockVariant.GRANITE) }
    val BRICK_PLATFORM: BlockDef = registerBlock("brick_platform") { PlatformBlock(BlockVariant.BRICK) }
    val STONE_BRICK_PLATFORM: BlockDef = registerBlock("stone_brick_platform") { PlatformBlock(BlockVariant.STONE_BRICK) }
    val RED_SANDSTONE_PLATFORM: BlockDef = registerBlock("red_sandstone_platform") { PlatformBlock(BlockVariant.RED_SANDSTONE) }
    val NETHER_BRICK_PLATFORM: BlockDef = registerBlock("nether_brick_platform") { PlatformBlock(BlockVariant.NETHER_BRICK) }
    val BASALT_PLATFORM: BlockDef = registerBlock("basalt_platform") { PlatformBlock(BlockVariant.BASALT) }
    val BLACKSTONE_PLATFORM: BlockDef = registerBlock("blackstone_platform") { PlatformBlock(BlockVariant.BLACKSTONE) }
    val RED_NETHER_BRICK_PLATFORM: BlockDef = registerBlock("red_nether_brick_platform") { PlatformBlock(BlockVariant.RED_NETHER_BRICK) }
    val PRISMARINE_PLATFORM: BlockDef = registerBlock("prismarine_platform") { PlatformBlock(BlockVariant.PRISMARINE) }
    val QUARTZ_PLATFORM: BlockDef = registerBlock("quartz_platform") { PlatformBlock(BlockVariant.QUARTZ) }
    val END_STONE_BRICK_PLATFORM: BlockDef = registerBlock("end_stone_brick_platform") { PlatformBlock(BlockVariant.END_STONE_BRICK) }
    val PURPUR_PLATFORM: BlockDef = registerBlock("purpur_platform") { PlatformBlock(BlockVariant.PURPUR) }
    val POLISHED_BLACKSTONE_PLATFORM: BlockDef = registerBlock("polished_blackstone_platform") { PlatformBlock(BlockVariant.POLISHED_BLACKSTONE) }
    val POLISHED_BLACKSTONE_BRICK_PLATFORM: BlockDef = registerBlock("polished_blackstone_brick_platform") { PlatformBlock(BlockVariant.POLISHED_BLACKSTONE_BRICK) }
    val POLISHED_DIORITE_PLATFORM: BlockDef = registerBlock("polished_diorite_platform") { PlatformBlock(BlockVariant.POLISHED_DIORITE) }
    val POLISHED_ANDESITE_PLATFORM: BlockDef = registerBlock("polished_andesite_platform") { PlatformBlock(BlockVariant.POLISHED_ANDESITE) }
    val POLISHED_GRANITE_PLATFORM: BlockDef = registerBlock("polished_granite_platform") { PlatformBlock(BlockVariant.POLISHED_GRANITE) }
    val PRISMARINE_BRICK_PLATFORM: BlockDef = registerBlock("prismarine_brick_platform") { PlatformBlock(BlockVariant.PRISMARINE_BRICK) }
    val DARK_PRISMARINE_PLATFORM: BlockDef = registerBlock("dark_prismarine_platform") { PlatformBlock(BlockVariant.DARK_PRISMARINE) }
    val CUT_SANDSTONE_PLATFORM: BlockDef = registerBlock("cut_sandstone_platform") { PlatformBlock(BlockVariant.CUT_SANDSTONE) }
    val SMOOTH_SANDSTONE_PLATFORM: BlockDef = registerBlock("smooth_sandstone_platform") { PlatformBlock(BlockVariant.SMOOTH_SANDSTONE) }
    val CUT_RED_SANDSTONE_PLATFORM: BlockDef = registerBlock("cut_red_sandstone_platform") { PlatformBlock(BlockVariant.CUT_RED_SANDSTONE) }
    val SMOOTH_RED_SANDSTONE_PLATFORM: BlockDef = registerBlock("smooth_red_sandstone_platform") { PlatformBlock(BlockVariant.SMOOTH_RED_SANDSTONE) }
    val SMOOTH_STONE_PLATFORM: BlockDef = registerBlock("smooth_stone_platform") { PlatformBlock(BlockVariant.SMOOTH_STONE) }
    val MOSSY_COBBLESTONE_PLATFORM: BlockDef = registerBlock("mossy_cobblestone_platform") { PlatformBlock(BlockVariant.MOSSY_COBBLESTONE) }
    val MOSSY_STONE_BRICK_PLATFORM: BlockDef = registerBlock("mossy_stone_brick_platform") { PlatformBlock(BlockVariant.MOSSY_STONE_BRICK) }

    val OAK_STEP: BlockDef = registerBlock("oak_step") { StepBlock(BlockVariant.OAK) }
    val SPRUCE_STEP: BlockDef = registerBlock("spruce_step") { StepBlock(BlockVariant.SPRUCE) }
    val BIRCH_STEP: BlockDef = registerBlock("birch_step") { StepBlock(BlockVariant.BIRCH) }
    val JUNGLE_STEP: BlockDef = registerBlock("jungle_step") { StepBlock(BlockVariant.JUNGLE) }
    val ACACIA_STEP: BlockDef = registerBlock("acacia_step") { StepBlock(BlockVariant.ACACIA) }
    val DARK_OAK_STEP: BlockDef = registerBlock("dark_oak_step") { StepBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_STEP: BlockDef = registerBlock("crimson_step") { StepBlock(BlockVariant.CRIMSON) }
    val WARPED_STEP: BlockDef = registerBlock("warped_step") { StepBlock(BlockVariant.WARPED) }
    val STONE_STEP: BlockDef = registerBlock("stone_step") { StepBlock(BlockVariant.STONE) }
    val COBBLESTONE_STEP: BlockDef = registerBlock("cobblestone_step") { StepBlock(BlockVariant.COBBLESTONE) }
    val SANDSTONE_STEP: BlockDef = registerBlock("sandstone_step") { StepBlock(BlockVariant.SANDSTONE) }
    val DIORITE_STEP: BlockDef = registerBlock("diorite_step") { StepBlock(BlockVariant.DIORITE) }
    val ANDESITE_STEP: BlockDef = registerBlock("andesite_step") { StepBlock(BlockVariant.ANDESITE) }
    val GRANITE_STEP: BlockDef = registerBlock("granite_step") { StepBlock(BlockVariant.GRANITE) }
    val BRICK_STEP: BlockDef = registerBlock("brick_step") { StepBlock(BlockVariant.BRICK) }
    val STONE_BRICK_STEP: BlockDef = registerBlock("stone_brick_step") { StepBlock(BlockVariant.STONE_BRICK) }
    val RED_SANDSTONE_STEP: BlockDef = registerBlock("red_sandstone_step") { StepBlock(BlockVariant.RED_SANDSTONE) }
    val NETHER_BRICK_STEP: BlockDef = registerBlock("nether_brick_step") { StepBlock(BlockVariant.NETHER_BRICK) }
    val BASALT_STEP: BlockDef = registerBlock("basalt_step") { StepBlock(BlockVariant.BASALT) }
    val BLACKSTONE_STEP: BlockDef = registerBlock("blackstone_step") { StepBlock(BlockVariant.BLACKSTONE) }
    val RED_NETHER_BRICK_STEP: BlockDef = registerBlock("red_nether_brick_step") { StepBlock(BlockVariant.RED_NETHER_BRICK) }
    val PRISMARINE_STEP: BlockDef = registerBlock("prismarine_step") { StepBlock(BlockVariant.PRISMARINE) }
    val QUARTZ_STEP: BlockDef = registerBlock("quartz_step") { StepBlock(BlockVariant.QUARTZ) }
    val END_STONE_BRICK_STEP: BlockDef = registerBlock("end_stone_brick_step") { StepBlock(BlockVariant.END_STONE_BRICK) }
    val PURPUR_STEP: BlockDef = registerBlock("purpur_step") { StepBlock(BlockVariant.PURPUR) }
    val POLISHED_BLACKSTONE_STEP: BlockDef = registerBlock("polished_blackstone_step") { StepBlock(BlockVariant.POLISHED_BLACKSTONE) }
    val POLISHED_BLACKSTONE_BRICK_STEP: BlockDef = registerBlock("polished_blackstone_brick_step") { StepBlock(BlockVariant.POLISHED_BLACKSTONE_BRICK) }
    val POLISHED_DIORITE_STEP: BlockDef = registerBlock("polished_diorite_step") { StepBlock(BlockVariant.POLISHED_DIORITE) }
    val POLISHED_ANDESITE_STEP: BlockDef = registerBlock("polished_andesite_step") { StepBlock(BlockVariant.POLISHED_ANDESITE) }
    val POLISHED_GRANITE_STEP: BlockDef = registerBlock("polished_granite_step") { StepBlock(BlockVariant.POLISHED_GRANITE) }
    val PRISMARINE_BRICK_STEP: BlockDef = registerBlock("prismarine_brick_step") { StepBlock(BlockVariant.PRISMARINE_BRICK) }
    val DARK_PRISMARINE_STEP: BlockDef = registerBlock("dark_prismarine_step") { StepBlock(BlockVariant.DARK_PRISMARINE) }
    val CUT_SANDSTONE_STEP: BlockDef = registerBlock("cut_sandstone_step") { StepBlock(BlockVariant.CUT_SANDSTONE) }
    val SMOOTH_SANDSTONE_STEP: BlockDef = registerBlock("smooth_sandstone_step") { StepBlock(BlockVariant.SMOOTH_SANDSTONE) }
    val CUT_RED_SANDSTONE_STEP: BlockDef = registerBlock("cut_red_sandstone_step") { StepBlock(BlockVariant.CUT_RED_SANDSTONE) }
    val SMOOTH_RED_SANDSTONE_STEP: BlockDef = registerBlock("smooth_red_sandstone_step") { StepBlock(BlockVariant.SMOOTH_RED_SANDSTONE) }
    val SMOOTH_STONE_STEP: BlockDef = registerBlock("smooth_stone_step") { StepBlock(BlockVariant.SMOOTH_STONE) }
    val MOSSY_COBBLESTONE_STEP: BlockDef = registerBlock("mossy_cobblestone_step") { StepBlock(BlockVariant.MOSSY_COBBLESTONE) }
    val MOSSY_STONE_BRICK_STEP: BlockDef = registerBlock("mossy_stone_brick_step") { StepBlock(BlockVariant.MOSSY_STONE_BRICK) }

    val OAK_SHELF: BlockDef = registerBlock("oak_shelf") { ShelfBlock(BlockVariant.OAK) }
    val SPRUCE_SHELF: BlockDef = registerBlock("spruce_shelf") { ShelfBlock(BlockVariant.SPRUCE) }
    val BIRCH_SHELF: BlockDef = registerBlock("birch_shelf") { ShelfBlock(BlockVariant.BIRCH) }
    val JUNGLE_SHELF: BlockDef = registerBlock("jungle_shelf") { ShelfBlock(BlockVariant.JUNGLE) }
    val ACACIA_SHELF: BlockDef = registerBlock("acacia_shelf") { ShelfBlock(BlockVariant.ACACIA) }
    val DARK_OAK_SHELF: BlockDef = registerBlock("dark_oak_shelf") { ShelfBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_SHELF: BlockDef = registerBlock("crimson_shelf") { ShelfBlock(BlockVariant.CRIMSON) }
    val WARPED_SHELF: BlockDef = registerBlock("warped_shelf") { ShelfBlock(BlockVariant.WARPED) }
    val IRON_SHELF: BlockDef = registerBlock("iron_shelf") { ShelfBlock(BlockVariant.IRON) }

    val BRICK_CHIMNEY: BlockDef = registerBlock("brick_chimney") { ChimneyBlock() }
    val STONE_BRICK_CHIMNEY: BlockDef = registerBlock("stone_brick_chimney") { ChimneyBlock() }
    val NETHER_BRICK_CHIMNEY: BlockDef = registerBlock("nether_brick_chimney") { ChimneyBlock() }
    val RED_NETHER_BRICK_CHIMNEY: BlockDef = registerBlock("red_nether_brick_chimney") { ChimneyBlock() }
    val COBBLESTONE_CHIMNEY: BlockDef = registerBlock("cobblestone_chimney") { ChimneyBlock() }
    val PRISMARINE_CHIMNEY: BlockDef = registerBlock("prismarine_chimney") {
        PrismarineChimneyBlock(AbstractBlock.Settings.copy(Blocks.PRISMARINE).ticksRandomly())
    }
    val MAGMATIC_PRISMARINE_CHIMNEY: BlockDef = registerBlock("magmatic_prismarine_chimney") {
        PrismarineChimneyBlock.WithColumn(
            true, AbstractBlock.Settings.copy(Blocks.PRISMARINE).ticksRandomly().luminance { 3 }
        )
    }
    val SOULFUL_PRISMARINE_CHIMNEY: BlockDef = registerBlock("soulful_prismarine_chimney") {
        PrismarineChimneyBlock.WithColumn(
            false, AbstractBlock.Settings.copy(Blocks.PRISMARINE).ticksRandomly()
        )
    }

    val OAK_KITCHEN_SINK: BlockDef = registerBlock("oak_kitchen_sink") { KitchenSinkBlock(BlockVariant.OAK) }
    val SPRUCE_KITCHEN_SINK: BlockDef = registerBlock("spruce_kitchen_sink") { KitchenSinkBlock(BlockVariant.SPRUCE) }
    val BIRCH_KITCHEN_SINK: BlockDef = registerBlock("birch_kitchen_sink") { KitchenSinkBlock(BlockVariant.BIRCH) }
    val JUNGLE_KITCHEN_SINK: BlockDef = registerBlock("jungle_kitchen_sink") { KitchenSinkBlock(BlockVariant.JUNGLE) }
    val ACACIA_KITCHEN_SINK: BlockDef = registerBlock("acacia_kitchen_sink") { KitchenSinkBlock(BlockVariant.ACACIA) }
    val DARK_OAK_KITCHEN_SINK: BlockDef = registerBlock("dark_oak_kitchen_sink") { KitchenSinkBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_KITCHEN_SINK: BlockDef = registerBlock("crimson_kitchen_sink") { KitchenSinkBlock(BlockVariant.CRIMSON) }
    val WARPED_KITCHEN_SINK: BlockDef = registerBlock("warped_kitchen_sink") { KitchenSinkBlock(BlockVariant.WARPED) }

    val OAK_COFFEE_TABLE: BlockDef = registerBlock("oak_coffee_table") { CoffeeTableBlock(BlockVariant.OAK) }
    val SPRUCE_COFFEE_TABLE: BlockDef = registerBlock("spruce_coffee_table") { CoffeeTableBlock(BlockVariant.SPRUCE) }
    val BIRCH_COFFEE_TABLE: BlockDef = registerBlock("birch_coffee_table") { CoffeeTableBlock(BlockVariant.BIRCH) }
    val JUNGLE_COFFEE_TABLE: BlockDef = registerBlock("jungle_coffee_table") { CoffeeTableBlock(BlockVariant.JUNGLE) }
    val ACACIA_COFFEE_TABLE: BlockDef = registerBlock("acacia_coffee_table") { CoffeeTableBlock(BlockVariant.ACACIA) }
    val DARK_OAK_COFFEE_TABLE: BlockDef = registerBlock("dark_oak_coffee_table") { CoffeeTableBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_COFFEE_TABLE: BlockDef = registerBlock("crimson_coffee_table") { CoffeeTableBlock(BlockVariant.CRIMSON) }
    val WARPED_COFFEE_TABLE: BlockDef = registerBlock("warped_coffee_table") { CoffeeTableBlock(BlockVariant.WARPED) }

    val OAK_BENCH: BlockDef = registerBlock("oak_bench") { BenchBlock(BlockVariant.OAK) }
    val SPRUCE_BENCH: BlockDef = registerBlock("spruce_bench") { BenchBlock(BlockVariant.SPRUCE) }
    val BIRCH_BENCH: BlockDef = registerBlock("birch_bench") { BenchBlock(BlockVariant.BIRCH) }
    val JUNGLE_BENCH: BlockDef = registerBlock("jungle_bench") { BenchBlock(BlockVariant.JUNGLE) }
    val ACACIA_BENCH: BlockDef = registerBlock("acacia_bench") { BenchBlock(BlockVariant.ACACIA) }
    val DARK_OAK_BENCH: BlockDef = registerBlock("dark_oak_bench") { BenchBlock(BlockVariant.DARK_OAK) }
    val CRIMSON_BENCH: BlockDef = registerBlock("crimson_bench") { BenchBlock(BlockVariant.CRIMSON) }
    val WARPED_BENCH: BlockDef = registerBlock("warped_bench") { BenchBlock(BlockVariant.WARPED) }

    val TABLE_LAMPS: Map<DyeColor, BlockDef> = DyeColor.values().associate {
        it to registerBlock("${it.asString()}_table_lamp") {
            TableLampBlock(TableLampBlock.createBlockSettings(it))
        }
    }

    val TRADING_STATION: BlockDef = registerBlock("trading_station") { TradingStationBlock() }

    val STONE_TORCH_GROUND: BlockDef = registerBlockWithoutItem("stone_torch") {
        TorchBlock(
            AbstractBlock.Settings.copy(Blocks.TORCH).sounds(BlockSoundGroup.STONE).luminance { 15 },
            ParticleTypes.FLAME
        )
    }
    val STONE_TORCH_WALL: BlockDef = registerBlockWithoutItem("wall_stone_torch") {
        WallTorchBlock(
            AbstractBlock.Settings.copy(STONE_TORCH_GROUND.get()).dropsLike(STONE_TORCH_GROUND.get()),
            ParticleTypes.FLAME
        )
    }

    val CRATE: BlockDef = registerBlock("crate") { Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)) }
    val APPLE_CRATE: BlockDef = registerCrate("apple_crate")
    val WHEAT_CRATE: BlockDef = registerCrate("wheat_crate")
    val CARROT_CRATE: BlockDef = registerCrate("carrot_crate")
    val POTATO_CRATE: BlockDef = registerCrate("potato_crate")
    val MELON_CRATE: BlockDef = registerCrate("melon_crate")
    val WHEAT_SEED_CRATE: BlockDef = registerCrate("wheat_seed_crate")
    val MELON_SEED_CRATE: BlockDef = registerCrate("melon_seed_crate")
    val PUMPKIN_SEED_CRATE: BlockDef = registerCrate("pumpkin_seed_crate")
    val BEETROOT_CRATE: BlockDef = registerCrate("beetroot_crate")
    val BEETROOT_SEED_CRATE: BlockDef = registerCrate("beetroot_seed_crate")
    val SWEET_BERRY_CRATE: BlockDef = registerCrate("sweet_berry_crate")
    val COCOA_BEAN_CRATE: BlockDef = registerCrate("cocoa_bean_crate")
    val NETHER_WART_CRATE: BlockDef = registerCrate("nether_wart_crate")
    val SUGAR_CANE_CRATE: BlockDef = registerCrate("sugar_cane_crate")
    val EGG_CRATE: BlockDef = registerCrate("egg_crate")
    val HONEYCOMB_CRATE: BlockDef = registerCrate("honeycomb_crate")
    val LIL_TATER_CRATE: BlockDef = registerCrate("lil_tater_crate")

    val PICKET_FENCE: BlockDef = registerBlock("picket_fence") {
        PicketFenceBlock(AbstractBlock.Settings.copy(Blocks.OAK_FENCE).nonOpaque())
    }
    val CHAIN_LINK_FENCE: BlockDef = registerBlock("chain_link_fence") {
        ChainLinkFenceBlock(
            AbstractBlock.Settings.copy(Blocks.IRON_BARS)
                .sounds(AdornSounds.CHAIN_LINK_FENCE)
        )
    }
    val STONE_LADDER: BlockDef = registerBlock("stone_ladder") {
        StoneLadderBlock(AbstractBlock.Settings.copy(Blocks.STONE).harvestTool(ToolType.PICKAXE).nonOpaque())
    }
    // @formatter:on

    @JvmStatic
    fun init() {
    }

    fun handleSneakClicks(event: PlayerInteractEvent.RightClickBlock) {
        val world = event.world
        val player = event.player
        val state = world.getBlockState(event.pos)
        val block = state.block

        // Check that:
        // - the block is a sneak-click handler
        // - the player is sneaking
        // - the player isn't holding an item (for block item and bucket support)
        if (block is SneakClickHandler && player.isSneaking && player.getStackInHand(event.hand).isEmpty) {
            val result = block.onSneakClick(state, world, event.pos, player, event.hand)

            if (result != ActionResult.PASS) {
                event.cancellationResult = result
                event.isCanceled = true
            }
        }
    }

    fun handleCarpetedBlocks(event: PlayerInteractEvent.RightClickBlock) {
        val world = event.world
        val pos = event.pos.offset(event.face)
        val state = world.getBlockState(event.pos)
        val block = state.block
        val player = event.player
        val hand = event.hand
        val stack = player.getStackInHand(hand)
        val item = stack.item

        if (block is CarpetedBlock && block.canStateBeCarpeted(state) && item is BlockItem) {
            val carpet = item.block
            if (carpet is CarpetBlock) {
                world.setBlockState(
                    pos,
                    state.with(CarpetedBlock.CARPET, CarpetedBlock.CARPET.wrapOrNone(carpet.color))
                )
                val soundGroup = carpet.defaultState.soundGroup
                world.playSound(
                    player,
                    pos,
                    soundGroup.placeSound,
                    SoundCategory.BLOCKS,
                    (soundGroup.volume + 1f) / 2f,
                    soundGroup.pitch * 0.8f
                )
                if (!player.abilities.creativeMode) stack.decrement(1)
                player.swingHand(hand)

                event.cancellationResult = ActionResult.SUCCESS
                event.isCanceled = true
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    fun initClient() {
        // BlockEntityRenderers
        ClientRegistry.bindTileEntityRenderer(
            AdornBlockEntities.TRADING_STATION.get(),
            ::TradingStationRenderer
        )
        ClientRegistry.bindTileEntityRenderer(
            AdornBlockEntities.SHELF.get(),
            ::ShelfRenderer
        )

        // RenderLayers
        val renderLayers = mapOf(
            RenderLayer.getCutout() to arrayOf(
                TRADING_STATION,
                STONE_TORCH_GROUND,
                STONE_TORCH_WALL,
                CHAIN_LINK_FENCE,
                STONE_LADDER
            ),
            RenderLayer.getTranslucent() to arrayOf(
                OAK_COFFEE_TABLE,
                SPRUCE_COFFEE_TABLE,
                BIRCH_COFFEE_TABLE,
                JUNGLE_COFFEE_TABLE,
                ACACIA_COFFEE_TABLE,
                DARK_OAK_COFFEE_TABLE,
                CRIMSON_COFFEE_TABLE,
                WARPED_COFFEE_TABLE
            )
        )

        for ((layer, blocks) in renderLayers) {
            for (block in blocks) {
                RenderLayers.setRenderLayer(block.get(), layer)
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    fun registerColorProviders(event: ColorHandlerEvent.Block) {
        event.blockColors.registerColorProvider(
            SinkColorProvider,
            OAK_KITCHEN_SINK.get(),
            SPRUCE_KITCHEN_SINK.get(),
            BIRCH_KITCHEN_SINK.get(),
            JUNGLE_KITCHEN_SINK.get(),
            ACACIA_KITCHEN_SINK.get(),
            DARK_OAK_KITCHEN_SINK.get(),
            CRIMSON_KITCHEN_SINK.get(),
            WARPED_KITCHEN_SINK.get()
        )
    }

    private fun registerBlock(name: String, itemGroup: ItemGroup = ItemGroup.DECORATIONS, block: () -> Block): BlockDef =
        registerBlock(name, Item.Settings().group(itemGroup), block)

    private fun registerBlock(name: String, itemSettings: Item.Settings, block: () -> Block): BlockDef =
        registerBlock(name, { makeItemForBlock(it, itemSettings) }, block)

    private inline fun registerBlock(
        name: String,
        crossinline itemProvider: (Block) -> Item,
        crossinline block: () -> Block
    ): BlockDef {
        val result = BLOCKS.register(name) { block() }
        ITEMS.register(name) { itemProvider(result.get()) }
        return result
    }

    /**
     * Registers a [block] with the [name] and without an item.
     */
    private inline fun registerBlockWithoutItem(name: String, crossinline block: () -> Block): BlockDef =
        BLOCKS.register(name) { block() }

    fun makeItemForBlock(block: Block, itemSettings: Item.Settings): Item =
        BaseBlockItem(block, itemSettings)

    private fun registerCrate(name: String): BlockDef = registerBlock(
        name,
        block = { Block(AbstractBlock.Settings.copy(CRATE.get())) },
        itemProvider = {
            makeItemForBlock(it, Item.Settings().group(ItemGroup.DECORATIONS).recipeRemainder(CRATE.get().asItem()))
        }
    )
}
