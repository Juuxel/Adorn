package juuxel.adorn.block;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.item.TradingStationItem;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.lib.registry.BlockRegistrar;
import juuxel.adorn.lib.registry.ItemRegistrar;
import juuxel.adorn.lib.registry.RegisteredBlock;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.lib.registry.RegistryHelper;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.util.AdornUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.WeatheringCopperCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class AdornBlocks {
    public static final BlockRegistrar BLOCKS = RegistrarFactory.get().createBlocks();
    public static final ItemRegistrar ITEMS = RegistrarFactory.get().createItems();
    private static final RegistryHelper HELPER = new RegistryHelper(BLOCKS, ITEMS);

    public static final ColorCollection<RegisteredBlock<SofaBlock>> SOFAS = Registrar.registerColored(
        "sofa",
        (id, color) -> HELPER.registerBlock(
            id,
            settings -> PlatformBridges.get().getBlockFactory().createSofa(settings),
            BlockVariant.wool(color)
        )
    );

    public static final ColorCollection<RegisteredBlock<Block>> PAINTED_PLANKS = Registrar.registerColored(
        "planks",
        (id, color) -> HELPER.registerBlock(
            id,
            settings -> PlatformBridges.get().getBlockFactory().createPaintedPlanks(settings),
            () -> BlockVariant.OAK.createBlockSettings().mapColor(color)
        )
    );

    public static final ColorCollection<RegisteredBlock<Block>> PAINTED_WOOD_SLABS = Registrar.registerColored(
        "wood_slab",
        (id, color) -> HELPER.registerBlock(
            id,
            settings -> PlatformBridges.get().getBlockFactory().createPaintedWoodSlab(settings),
            () -> BlockVariant.OAK.createBlockSettings().mapColor(color)
        )
    );

    public static final ColorCollection<RegisteredBlock<Block>> PAINTED_WOOD_STAIRS = Registrar.registerColored(
        "wood_stairs",
        (id, color) -> {
            var planks = PAINTED_PLANKS.pick(color);
            return HELPER.registerBlock(
                id,
                settings -> PlatformBridges.get().getBlockFactory().createPaintedWoodStairs(planks.get().defaultBlockState(), settings),
                () -> BlockVariant.OAK.createBlockSettings().mapColor(color)
            );
        }
    );

    public static final ColorCollection<RegisteredBlock<Block>> PAINTED_WOOD_FENCES = Registrar.registerColored(
        "wood_fence",
        (id, color) -> HELPER.registerBlock(
            id,
            settings -> PlatformBridges.get().getBlockFactory().createPaintedWoodFence(settings),
            () -> BlockVariant.OAK.createBlockSettings().mapColor(color)
        )
    );

    public static final ColorCollection<RegisteredBlock<Block>> PAINTED_WOOD_FENCE_GATES = Registrar.registerColored(
        "wood_fence_gate",
        (id, color) -> HELPER.registerBlock(
            id,
            settings -> PlatformBridges.get().getBlockFactory().createPaintedWoodFenceGate(
                AdornWoodTypes.PAINTED_WOODS.get(color),
                settings
            ),
            () -> BlockVariant.OAK.createBlockSettings().mapColor(color)
        )
    );

    public static final ColorCollection<RegisteredBlock<Block>> PAINTED_WOOD_PRESSURE_PLATES = Registrar.registerColored(
        "wood_pressure_plate",
        (id, color) -> HELPER.registerBlock(
            id,
            settings -> new PressurePlateBlock(AdornBlockSetTypes.PAINTED_WOODS.get(color), settings),
            () -> BlockVariant.OAK.createBlockSettings().mapColor(color)
        )
    );

    public static final ColorCollection<RegisteredBlock<Block>> PAINTED_WOOD_BUTTONS = Registrar.registerColored(
        "wood_button",
        (id, color) -> HELPER.registerBlock(
            id,
            settings -> new ButtonBlock(AdornBlockSetTypes.PAINTED_WOODS.get(color), 30, settings),
            () -> Blocks.buttonProperties().mapColor(color)
        )
    );

    public static final RegisteredBlock<Block> BRICK_CHIMNEY = HELPER.registerBlock("brick_chimney",
        ChimneyBlock::new,
        () -> AbstractChimneyBlock.createBlockSettings(MapColor.COLOR_RED)
    );
    public static final RegisteredBlock<Block> STONE_BRICK_CHIMNEY = HELPER.registerBlock("stone_brick_chimney",
        ChimneyBlock::new,
        () -> AbstractChimneyBlock.createBlockSettings(MapColor.STONE)
    );
    public static final RegisteredBlock<Block> NETHER_BRICK_CHIMNEY = HELPER.registerBlock("nether_brick_chimney",
        ChimneyBlock::new,
        () -> AbstractChimneyBlock.createBlockSettings(MapColor.NETHER)
    );
    public static final RegisteredBlock<Block> RED_NETHER_BRICK_CHIMNEY = HELPER.registerBlock("red_nether_brick_chimney",
        ChimneyBlock::new,
        () -> AbstractChimneyBlock.createBlockSettings(MapColor.NETHER)
    );
    public static final RegisteredBlock<Block> COBBLESTONE_CHIMNEY = HELPER.registerBlock("cobblestone_chimney",
        ChimneyBlock::new,
        () -> AbstractChimneyBlock.createBlockSettings(MapColor.STONE)
    );
    public static final RegisteredBlock<Block> PRISMARINE_CHIMNEY = HELPER.registerBlock("prismarine_chimney",
        PrismarineChimneyBlock::new,
        () -> AbstractChimneyBlock.createBlockSettings(MapColor.COLOR_CYAN, 1.5f)
    );
    public static final RegisteredBlock<Block> MAGMATIC_PRISMARINE_CHIMNEY = HELPER.registerBlock("magmatic_prismarine_chimney",
        settings -> new PrismarineChimneyBlock.WithColumn(true, settings),
        () -> AbstractChimneyBlock.createBlockSettings(MapColor.COLOR_CYAN, 1.5f).lightLevel(state -> 3)
    );
    public static final RegisteredBlock<Block> SOULFUL_PRISMARINE_CHIMNEY = HELPER.registerBlock("soulful_prismarine_chimney",
        settings -> new PrismarineChimneyBlock.WithColumn(false, settings),
        () -> AbstractChimneyBlock.createBlockSettings(MapColor.COLOR_CYAN, 1.5f)
    );

    public static final ColorCollection<RegisteredBlock<Block>> TABLE_LAMPS = Registrar.registerColored(
        "table_lamp",
        (id, color) -> HELPER.registerBlock(id, TableLampBlock::new, () -> TableLampBlock.createBlockSettings(color))
    );

    public static final RegisteredBlock<Block> TRADING_STATION = HELPER.registerBlock(
        "trading_station",
        TradingStationItem::new,
        TradingStationBlock::new,
        () -> BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(2.5f).sound(SoundType.WOOD)
    );

    public static final RegisteredBlock<Block> STONE_TORCH_GROUND = HELPER.registerBlockWithoutItem("stone_torch",
        settings -> new TorchBlock(ParticleTypes.FLAME, settings),
        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH)
            .sound(SoundType.STONE)
            .lightLevel(state -> 15)
    );

    public static final RegisteredBlock<Block> STONE_TORCH_WALL = HELPER.registerBlockWithoutItem("wall_stone_torch",
        settings -> new WallTorchBlock(ParticleTypes.FLAME, settings),
        () -> alternativeFormOf(BlockBehaviour.Properties.ofFullCopy(STONE_TORCH_GROUND.get()), STONE_TORCH_GROUND.get())
    );

    public static final RegisteredBlock<Block> CRATE = HELPER.registerBlock("crate",
        Block::new,
        () -> AdornUtil.copySettingsSafely(Blocks.OAK_PLANKS)
    );
    public static final RegisteredBlock<Block> APPLE_CRATE = registerCrate("apple_crate");
    public static final RegisteredBlock<Block> WHEAT_CRATE = registerCrate("wheat_crate");
    public static final RegisteredBlock<Block> CARROT_CRATE = registerCrate("carrot_crate");
    public static final RegisteredBlock<Block> POTATO_CRATE = registerCrate("potato_crate");
    public static final RegisteredBlock<Block> MELON_CRATE = registerCrate("melon_crate");
    public static final RegisteredBlock<Block> WHEAT_SEED_CRATE = registerCrate("wheat_seed_crate");
    public static final RegisteredBlock<Block> MELON_SEED_CRATE = registerCrate("melon_seed_crate");
    public static final RegisteredBlock<Block> PUMPKIN_SEED_CRATE = registerCrate("pumpkin_seed_crate");
    public static final RegisteredBlock<Block> BEETROOT_CRATE = registerCrate("beetroot_crate");
    public static final RegisteredBlock<Block> BEETROOT_SEED_CRATE = registerCrate("beetroot_seed_crate");
    public static final RegisteredBlock<Block> SWEET_BERRY_CRATE = registerCrate("sweet_berry_crate");
    public static final RegisteredBlock<Block> COCOA_BEAN_CRATE = registerCrate("cocoa_bean_crate");
    public static final RegisteredBlock<Block> NETHER_WART_CRATE = registerCrate("nether_wart_crate");
    public static final RegisteredBlock<Block> SUGAR_CANE_CRATE = registerCrate("sugar_cane_crate");
    public static final RegisteredBlock<Block> EGG_CRATE = registerCrate("egg_crate");
    public static final RegisteredBlock<Block> HONEYCOMB_CRATE = registerCrate("honeycomb_crate");
    public static final RegisteredBlock<Block> LIL_TATER_CRATE = registerCrate("lil_tater_crate");

    public static final RegisteredBlock<Block> PICKET_FENCE = HELPER.registerBlock("picket_fence",
        PicketFenceBlock::new,
        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).noOcclusion()
    );
    public static final RegisteredBlock<Block> CHAIN_LINK_FENCE = HELPER.registerBlock("chain_link_fence",
        ChainLinkFenceBlock::new,
        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS)
            .sound(AdornSounds.CHAIN_LINK_FENCE)
    );
    public static final RegisteredBlock<Block> STONE_LADDER = HELPER.registerBlock("stone_ladder",
        StoneLadderBlock::new,
        () -> BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion()
    );
    public static final RegisteredBlock<Block> BREWER = HELPER.registerBlock("brewer",
        BrewerBlock::new,
        () -> BlockBehaviour.Properties.of()
            .mapColor(MapColor.DEEPSLATE)
            .forceSolidOn()
            .strength(0.8F)
            .requiresCorrectToolForDrops()
    );

    public static final RegisteredBlock<Block> CANDLELIT_LANTERN = HELPER.registerBlock("candlelit_lantern",
        CandlelitLanternBlock::new,
        CandlelitLanternBlock::createBlockSettings
    );
    public static final ColorCollection<RegisteredBlock<Block>> DYED_CANDLELIT_LANTERNS = Registrar.registerColored(
        "candlelit_lantern",
        (id, _) -> HELPER.registerBlock(
            id,
            CandlelitLanternBlock::new,
            CandlelitLanternBlock::createBlockSettings
        )
    );

    public static final WeatheringCopperCollection<RegisteredBlock<Block>> COPPER_PIPES = Registrar.registerWeatheringCopper(
        "copper_pipe",
        (id, state) -> HELPER.registerBlock(
            id,
            settings -> new OxidizableCopperPipeBlock(state, settings),
            () -> BlockBehaviour.Properties.of()
                .requiresCorrectToolForDrops()
                .strength(3f, 5f)
                .sound(SoundType.COPPER)
                .mapColor(switch (state) {
                    case UNAFFECTED -> MapColor.COLOR_ORANGE;
                    case EXPOSED -> MapColor.TERRACOTTA_LIGHT_GRAY;
                    case WEATHERED -> MapColor.WARPED_STEM;
                    case OXIDIZED -> MapColor.WARPED_NYLIUM;
                })
        ),
        (id, base) -> HELPER.registerBlock(
            id,
            CopperPipeBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(base.get())
        )
    );

    public static final RegisteredBlock<Block> BARRICADE = HELPER.registerBlock("barricade",
        BarricadeBlock::new,
        () -> BlockBehaviour.Properties.of()
            .strength(2f, 3f)
            .sound(SoundType.IRON)
            .noOcclusion()
            .mapColor(DyeColor.RED)
    );

    public static final RegisteredBlock<Block> CAUTION_SIGN = HELPER.registerBlockWithoutItem("caution_sign",
        StandingCautionSignBlock::new,
        StandingCautionSignBlock::createBlockSettings
    );
    public static final RegisteredBlock<Block> BEE_CAUTION_SIGN = HELPER.registerBlockWithoutItem("bee_caution_sign",
        StandingCautionSignBlock::new,
        StandingCautionSignBlock::createBlockSettings
    );
    public static final RegisteredBlock<Block> BOOK_CAUTION_SIGN = HELPER.registerBlockWithoutItem("book_caution_sign",
        StandingCautionSignBlock::new,
        StandingCautionSignBlock::createBlockSettings
    );
    public static final RegisteredBlock<Block> CLIFF_CAUTION_SIGN = HELPER.registerBlockWithoutItem("cliff_caution_sign",
        StandingCautionSignBlock::new,
        StandingCautionSignBlock::createBlockSettings
    );
    public static final RegisteredBlock<Block> FORBIDDEN_CAUTION_SIGN = HELPER.registerBlockWithoutItem("forbidden_caution_sign",
        StandingCautionSignBlock::new,
        StandingCautionSignBlock::createBlockSettings
    );
    public static final RegisteredBlock<Block> HELMET_CAUTION_SIGN = HELPER.registerBlockWithoutItem("helmet_caution_sign",
        StandingCautionSignBlock::new,
        StandingCautionSignBlock::createBlockSettings
    );
    public static final RegisteredBlock<Block> RAILS_CAUTION_SIGN = HELPER.registerBlockWithoutItem("rails_caution_sign",
        StandingCautionSignBlock::new,
        StandingCautionSignBlock::createBlockSettings
    );
    public static final RegisteredBlock<Block> SURPRISE_CAUTION_SIGN = HELPER.registerBlockWithoutItem("surprise_caution_sign",
        StandingCautionSignBlock::new,
        StandingCautionSignBlock::createBlockSettings
    );
    public static final RegisteredBlock<Block> WALL_CAUTION_SIGN = HELPER.registerBlockWithoutItem("wall_caution_sign",
        WallCautionSignBlock::new,
        () -> WallCautionSignBlock.createBlockSettings(CAUTION_SIGN)
    );
    public static final RegisteredBlock<Block> BEE_WALL_CAUTION_SIGN = HELPER.registerBlockWithoutItem("bee_wall_caution_sign",
        WallCautionSignBlock::new,
        () -> WallCautionSignBlock.createBlockSettings(BEE_CAUTION_SIGN)
    );
    public static final RegisteredBlock<Block> BOOK_WALL_CAUTION_SIGN = HELPER.registerBlockWithoutItem("book_wall_caution_sign",
        WallCautionSignBlock::new,
        () -> WallCautionSignBlock.createBlockSettings(BOOK_CAUTION_SIGN)
    );
    public static final RegisteredBlock<Block> CLIFF_WALL_CAUTION_SIGN = HELPER.registerBlockWithoutItem("cliff_wall_caution_sign",
        WallCautionSignBlock::new,
        () -> WallCautionSignBlock.createBlockSettings(CLIFF_CAUTION_SIGN)
    );
    public static final RegisteredBlock<Block> FORBIDDEN_WALL_CAUTION_SIGN = HELPER.registerBlockWithoutItem("forbidden_wall_caution_sign",
        WallCautionSignBlock::new,
        () -> WallCautionSignBlock.createBlockSettings(FORBIDDEN_CAUTION_SIGN)
    );
    public static final RegisteredBlock<Block> HELMET_WALL_CAUTION_SIGN = HELPER.registerBlockWithoutItem("helmet_wall_caution_sign",
        WallCautionSignBlock::new,
        () -> WallCautionSignBlock.createBlockSettings(HELMET_CAUTION_SIGN)
    );
    public static final RegisteredBlock<Block> RAILS_WALL_CAUTION_SIGN = HELPER.registerBlockWithoutItem("rails_wall_caution_sign",
        WallCautionSignBlock::new,
        () -> WallCautionSignBlock.createBlockSettings(RAILS_CAUTION_SIGN)
    );
    public static final RegisteredBlock<Block> SURPRISE_WALL_CAUTION_SIGN = HELPER.registerBlockWithoutItem("surprise_wall_caution_sign",
        WallCautionSignBlock::new,
        () -> WallCautionSignBlock.createBlockSettings(SURPRISE_CAUTION_SIGN)
    );

    public static void init() {
    }

    private static RegisteredBlock<Block> registerCrate(String name) {
        return HELPER.registerBlock(name, () -> new Item.Properties().craftRemainder(CRATE.get().asItem()), Block::new, () -> AdornUtil.copySettingsSafely(CRATE.get()));
    }

    private static BlockBehaviour.Properties alternativeFormOf(BlockBehaviour.Properties settings, Block other) {
        return settings.overrideLootTable(other.getLootTable()).overrideDescription(other.getDescriptionId());
    }
}
