package juuxel.adorn.block

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.item.TradingStationItem
import juuxel.adorn.lib.AdornSounds
import juuxel.adorn.lib.registry.Registered
import juuxel.adorn.lib.registry.RegistryHelper
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.associateLazily
import juuxel.adorn.util.copySettingsSafely
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.MapColor
import net.minecraft.block.Oxidizable
import net.minecraft.block.TorchBlock
import net.minecraft.block.WallTorchBlock
import net.minecraft.item.Item
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.DyeColor

@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
object AdornBlocks : RegistryHelper() {
    val SOFAS: Map<DyeColor, SofaBlock> by DyeColor.values().associateLazily {
        registerBlock(it.asString() + "_sofa") { PlatformBridges.blockFactory.createSofa(BlockVariant.wool(it)) }
    }

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

    val TABLE_LAMPS: Map<DyeColor, Block> by DyeColor.values().associateLazily {
        registerBlock("${it.asString()}_table_lamp") {
            TableLampBlock(TableLampBlock.createBlockSettings(it))
        }
    }

    val TRADING_STATION: Block by registerBlock(
        "trading_station",
        itemProvider = { TradingStationItem(it, Item.Settings()) },
        block = { TradingStationBlock(AbstractBlock.Settings.create().mapColor(MapColor.GREEN).strength(2.5f).sounds(BlockSoundGroup.WOOD)) }
    )

    val STONE_TORCH_GROUND: Block by registerBlockWithoutItem("stone_torch") {
        TorchBlock(
            ParticleTypes.FLAME,
            AbstractBlock.Settings.copy(Blocks.TORCH)
                .sounds(BlockSoundGroup.STONE)
                .luminance { 15 }
        )
    }
    val STONE_TORCH_WALL: Block by registerBlockWithoutItem("wall_stone_torch") {
        WallTorchBlock(
            ParticleTypes.FLAME,
            AbstractBlock.Settings.copy(STONE_TORCH_GROUND).dropsLike(STONE_TORCH_GROUND)
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
    val BREWER: Block by registerBlock("brewer") {
        BrewerBlock(
            AbstractBlock.Settings.create()
                .mapColor(MapColor.DEEPSLATE_GRAY)
                .solid()
                .strength(0.8F)
                .requiresTool()
        )
    }

    val CANDLELIT_LANTERN: Block by registerBlock("candlelit_lantern") {
        CandlelitLanternBlock(CandlelitLanternBlock.createBlockSettings())
    }
    val DYED_CANDLELIT_LANTERNS: Map<DyeColor, Block> by DyeColor.values().associateLazily {
        registerBlock("${it.asString()}_candlelit_lantern") {
            CandlelitLanternBlock(CandlelitLanternBlock.createBlockSettings())
        }
    }

    val COPPER_PIPE: Block by registerBlock("copper_pipe") {
        OxidizableCopperPipeBlock(
            Oxidizable.OxidationLevel.UNAFFECTED,
            AbstractBlock.Settings.create()
                .requiresTool()
                .strength(3f, 5f)
                .sounds(BlockSoundGroup.COPPER)
                .mapColor(MapColor.ORANGE)
        )
    }

    val EXPOSED_COPPER_PIPE: Block by registerBlock("exposed_copper_pipe") {
        OxidizableCopperPipeBlock(
            Oxidizable.OxidationLevel.EXPOSED,
            AbstractBlock.Settings.create()
                .requiresTool()
                .strength(3f, 5f)
                .sounds(BlockSoundGroup.COPPER)
                .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
        )
    }

    val WEATHERED_COPPER_PIPE: Block by registerBlock("weathered_copper_pipe") {
        OxidizableCopperPipeBlock(
            Oxidizable.OxidationLevel.WEATHERED,
            AbstractBlock.Settings.create()
                .requiresTool()
                .strength(3f, 5f)
                .sounds(BlockSoundGroup.COPPER)
                .mapColor(MapColor.DARK_AQUA)
        )
    }

    val OXIDIZED_COPPER_PIPE: Block by registerBlock("oxidized_copper_pipe") {
        OxidizableCopperPipeBlock(
            Oxidizable.OxidationLevel.OXIDIZED,
            AbstractBlock.Settings.create()
                .requiresTool()
                .strength(3f, 5f)
                .sounds(BlockSoundGroup.COPPER)
                .mapColor(MapColor.TEAL)
        )
    }

    val WAXED_COPPER_PIPE: Block by registerBlock("waxed_copper_pipe") {
        CopperPipeBlock(AbstractBlock.Settings.copy(COPPER_PIPE))
    }

    val WAXED_EXPOSED_COPPER_PIPE: Block by registerBlock("waxed_exposed_copper_pipe") {
        CopperPipeBlock(AbstractBlock.Settings.copy(EXPOSED_COPPER_PIPE))
    }

    val WAXED_WEATHERED_COPPER_PIPE: Block by registerBlock("waxed_weathered_copper_pipe") {
        CopperPipeBlock(AbstractBlock.Settings.copy(WEATHERED_COPPER_PIPE))
    }

    val WAXED_OXIDIZED_COPPER_PIPE: Block by registerBlock("waxed_oxidized_copper_pipe") {
        CopperPipeBlock(AbstractBlock.Settings.copy(OXIDIZED_COPPER_PIPE))
    }

    fun init() {
    }

    private fun registerCrate(name: String): Registered<Block> =
        registerBlock(
            name,
            itemSettings = { Item.Settings().recipeRemainder(CRATE.asItem()) },
            block = { Block(CRATE.copySettingsSafely()) }
        )
}
