package juuxel.adorn.block

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.renderer.ShelfRenderer
import juuxel.adorn.block.renderer.TradingStationRenderer
import juuxel.adorn.client.SinkColorProvider
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.lib.RegistryHelper
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.CarpetBlock
import net.minecraft.block.Material
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.item.BlockItem
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor

object AdornBlocks : RegistryHelper(Adorn.NAMESPACE) {
    val SOFAS: List<SofaBlock> = BlockVariant.WOOLS.values.map {
        // This is one place where the BlockVariant mapping is kept.
        // I will not write out sixteen sofa registrations.
        registerBlock(it.variantName + "_sofa", SofaBlock(it))
    }

    val OAK_CHAIR: Block = registerBlock("oak_chair", ChairBlock(BlockVariant.OAK), ::ChairBlockItem)
    val SPRUCE_CHAIR: Block = registerBlock("spruce_chair", ChairBlock(BlockVariant.SPRUCE), ::ChairBlockItem)
    val BIRCH_CHAIR: Block = registerBlock("birch_chair", ChairBlock(BlockVariant.BIRCH), ::ChairBlockItem)
    val JUNGLE_CHAIR: Block = registerBlock("jungle_chair", ChairBlock(BlockVariant.JUNGLE), ::ChairBlockItem)
    val ACACIA_CHAIR: Block = registerBlock("acacia_chair", ChairBlock(BlockVariant.ACACIA), ::ChairBlockItem)
    val DARK_OAK_CHAIR: Block = registerBlock("dark_oak_chair", ChairBlock(BlockVariant.DARK_OAK), ::ChairBlockItem)

    val OAK_TABLE: Block = registerBlock("oak_table", TableBlock(BlockVariant.OAK), ::TableBlockItem)
    val SPRUCE_TABLE: Block = registerBlock("spruce_table", TableBlock(BlockVariant.SPRUCE), ::TableBlockItem)
    val BIRCH_TABLE: Block = registerBlock("birch_table", TableBlock(BlockVariant.BIRCH), ::TableBlockItem)
    val JUNGLE_TABLE: Block = registerBlock("jungle_table", TableBlock(BlockVariant.JUNGLE), ::TableBlockItem)
    val ACACIA_TABLE: Block = registerBlock("acacia_table", TableBlock(BlockVariant.ACACIA), ::TableBlockItem)
    val DARK_OAK_TABLE: Block = registerBlock("dark_oak_table", TableBlock(BlockVariant.DARK_OAK), ::TableBlockItem)

    val OAK_KITCHEN_COUNTER: Block = registerBlock("oak_kitchen_counter", KitchenCounterBlock())
    val SPRUCE_KITCHEN_COUNTER: Block = registerBlock("spruce_kitchen_counter", KitchenCounterBlock())
    val BIRCH_KITCHEN_COUNTER: Block = registerBlock("birch_kitchen_counter", KitchenCounterBlock())
    val JUNGLE_KITCHEN_COUNTER: Block = registerBlock("jungle_kitchen_counter", KitchenCounterBlock())
    val ACACIA_KITCHEN_COUNTER: Block = registerBlock("acacia_kitchen_counter", KitchenCounterBlock())
    val DARK_OAK_KITCHEN_COUNTER: Block = registerBlock("dark_oak_kitchen_counter", KitchenCounterBlock())

    val OAK_KITCHEN_CUPBOARD: Block = registerBlock("oak_kitchen_cupboard", KitchenCupboardBlock())
    val SPRUCE_KITCHEN_CUPBOARD: Block = registerBlock("spruce_kitchen_cupboard", KitchenCupboardBlock())
    val BIRCH_KITCHEN_CUPBOARD: Block = registerBlock("birch_kitchen_cupboard", KitchenCupboardBlock())
    val JUNGLE_KITCHEN_CUPBOARD: Block = registerBlock("jungle_kitchen_cupboard", KitchenCupboardBlock())
    val ACACIA_KITCHEN_CUPBOARD: Block = registerBlock("acacia_kitchen_cupboard", KitchenCupboardBlock())
    val DARK_OAK_KITCHEN_CUPBOARD: Block = registerBlock("dark_oak_kitchen_cupboard", KitchenCupboardBlock())

    val OAK_DRAWER: Block = registerBlock("oak_drawer", DrawerBlock(BlockVariant.OAK))
    val SPRUCE_DRAWER: Block = registerBlock("spruce_drawer", DrawerBlock(BlockVariant.SPRUCE))
    val BIRCH_DRAWER: Block = registerBlock("birch_drawer", DrawerBlock(BlockVariant.BIRCH))
    val JUNGLE_DRAWER: Block = registerBlock("jungle_drawer", DrawerBlock(BlockVariant.JUNGLE))
    val ACACIA_DRAWER: Block = registerBlock("acacia_drawer", DrawerBlock(BlockVariant.ACACIA))
    val DARK_OAK_DRAWER: Block = registerBlock("dark_oak_drawer", DrawerBlock(BlockVariant.DARK_OAK))

    val OAK_POST: Block = registerBlock("oak_post", PostBlock(BlockVariant.OAK))
    val SPRUCE_POST: Block = registerBlock("spruce_post", PostBlock(BlockVariant.SPRUCE))
    val BIRCH_POST: Block = registerBlock("birch_post", PostBlock(BlockVariant.BIRCH))
    val JUNGLE_POST: Block = registerBlock("jungle_post", PostBlock(BlockVariant.JUNGLE))
    val ACACIA_POST: Block = registerBlock("acacia_post", PostBlock(BlockVariant.ACACIA))
    val DARK_OAK_POST: Block = registerBlock("dark_oak_post", PostBlock(BlockVariant.DARK_OAK))
    val STONE_POST: Block = registerBlock("stone_post", PostBlock(BlockVariant.STONE))
    val COBBLESTONE_POST: Block = registerBlock("cobblestone_post", PostBlock(BlockVariant.COBBLESTONE))
    val SANDSTONE_POST: Block = registerBlock("sandstone_post", PostBlock(BlockVariant.SANDSTONE))
    val DIORITE_POST: Block = registerBlock("diorite_post", PostBlock(BlockVariant.DIORITE))
    val ANDESITE_POST: Block = registerBlock("andesite_post", PostBlock(BlockVariant.ANDESITE))
    val GRANITE_POST: Block = registerBlock("granite_post", PostBlock(BlockVariant.GRANITE))
    val BRICK_POST: Block = registerBlock("brick_post", PostBlock(BlockVariant.BRICK))
    val STONE_BRICK_POST: Block = registerBlock("stone_brick_post", PostBlock(BlockVariant.STONE_BRICK))
    val RED_SANDSTONE_POST: Block = registerBlock("red_sandstone_post", PostBlock(BlockVariant.RED_SANDSTONE))
    val NETHER_BRICK_POST: Block = registerBlock("nether_brick_post", PostBlock(BlockVariant.NETHER_BRICK))

    val OAK_PLATFORM: Block = registerBlock("oak_platform", PlatformBlock(BlockVariant.OAK))
    val SPRUCE_PLATFORM: Block = registerBlock("spruce_platform", PlatformBlock(BlockVariant.SPRUCE))
    val BIRCH_PLATFORM: Block = registerBlock("birch_platform", PlatformBlock(BlockVariant.BIRCH))
    val JUNGLE_PLATFORM: Block = registerBlock("jungle_platform", PlatformBlock(BlockVariant.JUNGLE))
    val ACACIA_PLATFORM: Block = registerBlock("acacia_platform", PlatformBlock(BlockVariant.ACACIA))
    val DARK_OAK_PLATFORM: Block = registerBlock("dark_oak_platform", PlatformBlock(BlockVariant.DARK_OAK))
    val STONE_PLATFORM: Block = registerBlock("stone_platform", PlatformBlock(BlockVariant.STONE))
    val COBBLESTONE_PLATFORM: Block = registerBlock("cobblestone_platform", PlatformBlock(BlockVariant.COBBLESTONE))
    val SANDSTONE_PLATFORM: Block = registerBlock("sandstone_platform", PlatformBlock(BlockVariant.SANDSTONE))
    val DIORITE_PLATFORM: Block = registerBlock("diorite_platform", PlatformBlock(BlockVariant.DIORITE))
    val ANDESITE_PLATFORM: Block = registerBlock("andesite_platform", PlatformBlock(BlockVariant.ANDESITE))
    val GRANITE_PLATFORM: Block = registerBlock("granite_platform", PlatformBlock(BlockVariant.GRANITE))
    val BRICK_PLATFORM: Block = registerBlock("brick_platform", PlatformBlock(BlockVariant.BRICK))
    val STONE_BRICK_PLATFORM: Block = registerBlock("stone_brick_platform", PlatformBlock(BlockVariant.STONE_BRICK))
    val RED_SANDSTONE_PLATFORM: Block =
        registerBlock("red_sandstone_platform", PlatformBlock(BlockVariant.RED_SANDSTONE))
    val NETHER_BRICK_PLATFORM: Block = registerBlock("nether_brick_platform", PlatformBlock(BlockVariant.NETHER_BRICK))

    val OAK_STEP: Block = registerBlock("oak_step", StepBlock(BlockVariant.OAK))
    val SPRUCE_STEP: Block = registerBlock("spruce_step", StepBlock(BlockVariant.SPRUCE))
    val BIRCH_STEP: Block = registerBlock("birch_step", StepBlock(BlockVariant.BIRCH))
    val JUNGLE_STEP: Block = registerBlock("jungle_step", StepBlock(BlockVariant.JUNGLE))
    val ACACIA_STEP: Block = registerBlock("acacia_step", StepBlock(BlockVariant.ACACIA))
    val DARK_OAK_STEP: Block = registerBlock("dark_oak_step", StepBlock(BlockVariant.DARK_OAK))
    val STONE_STEP: Block = registerBlock("stone_step", StepBlock(BlockVariant.STONE))
    val COBBLESTONE_STEP: Block = registerBlock("cobblestone_step", StepBlock(BlockVariant.COBBLESTONE))
    val SANDSTONE_STEP: Block = registerBlock("sandstone_step", StepBlock(BlockVariant.SANDSTONE))
    val DIORITE_STEP: Block = registerBlock("diorite_step", StepBlock(BlockVariant.DIORITE))
    val ANDESITE_STEP: Block = registerBlock("andesite_step", StepBlock(BlockVariant.ANDESITE))
    val GRANITE_STEP: Block = registerBlock("granite_step", StepBlock(BlockVariant.GRANITE))
    val BRICK_STEP: Block = registerBlock("brick_step", StepBlock(BlockVariant.BRICK))
    val STONE_BRICK_STEP: Block = registerBlock("stone_brick_step", StepBlock(BlockVariant.STONE_BRICK))
    val RED_SANDSTONE_STEP: Block = registerBlock("red_sandstone_step", StepBlock(BlockVariant.RED_SANDSTONE))
    val NETHER_BRICK_STEP: Block = registerBlock("nether_brick_step", StepBlock(BlockVariant.NETHER_BRICK))

    val OAK_SHELF: ShelfBlock = registerBlock("oak_shelf", ShelfBlock(BlockVariant.OAK))
    val SPRUCE_SHELF: ShelfBlock = registerBlock("spruce_shelf", ShelfBlock(BlockVariant.SPRUCE))
    val BIRCH_SHELF: ShelfBlock = registerBlock("birch_shelf", ShelfBlock(BlockVariant.BIRCH))
    val JUNGLE_SHELF: ShelfBlock = registerBlock("jungle_shelf", ShelfBlock(BlockVariant.JUNGLE))
    val ACACIA_SHELF: ShelfBlock = registerBlock("acacia_shelf", ShelfBlock(BlockVariant.ACACIA))
    val DARK_OAK_SHELF: ShelfBlock = registerBlock("dark_oak_shelf", ShelfBlock(BlockVariant.DARK_OAK))
    val IRON_SHELF: ShelfBlock = registerBlock("iron_shelf", ShelfBlock(BlockVariant.IRON))

    val BRICK_CHIMNEY: Block = registerBlock("brick_chimney", ChimneyBlock())
    val STONE_BRICK_CHIMNEY: Block = registerBlock("stone_brick_chimney", ChimneyBlock())
    val NETHER_BRICK_CHIMNEY: Block = registerBlock("nether_brick_chimney", ChimneyBlock())
    val RED_NETHER_BRICK_CHIMNEY: Block = registerBlock("red_nether_brick_chimney", ChimneyBlock())
    val COBBLESTONE_CHIMNEY: Block = registerBlock("cobblestone_chimney", ChimneyBlock())
    val PRISMARINE_CHIMNEY: Block = registerBlock(
        "prismarine_chimney",
        PrismarineChimneyBlock(FabricBlockSettings.copy(Blocks.PRISMARINE).ticksRandomly().build())
    )
    val MAGMATIC_PRISMARINE_CHIMNEY: Block = registerBlock(
        "magmatic_prismarine_chimney",
        PrismarineChimneyBlock.WithColumn(
            true, FabricBlockSettings.copy(Blocks.PRISMARINE).ticksRandomly().lightLevel(3).build()
        )
    )
    val SOULFUL_PRISMARINE_CHIMNEY: Block = registerBlock(
        "soulful_prismarine_chimney",
        PrismarineChimneyBlock.WithColumn(
            false, FabricBlockSettings.copy(Blocks.PRISMARINE).ticksRandomly().build()
        )
    )

    val OAK_KITCHEN_SINK: Block = registerBlock("oak_kitchen_sink", KitchenSinkBlock(BlockVariant.OAK))
    val SPRUCE_KITCHEN_SINK: Block = registerBlock("spruce_kitchen_sink", KitchenSinkBlock(BlockVariant.SPRUCE))
    val BIRCH_KITCHEN_SINK: Block = registerBlock("birch_kitchen_sink", KitchenSinkBlock(BlockVariant.BIRCH))
    val JUNGLE_KITCHEN_SINK: Block = registerBlock("jungle_kitchen_sink", KitchenSinkBlock(BlockVariant.JUNGLE))
    val ACACIA_KITCHEN_SINK: Block = registerBlock("acacia_kitchen_sink", KitchenSinkBlock(BlockVariant.ACACIA))
    val DARK_OAK_KITCHEN_SINK: Block = registerBlock("dark_oak_kitchen_sink", KitchenSinkBlock(BlockVariant.DARK_OAK))

    val TABLE_LAMPS: Map<DyeColor, Block> = DyeColor.values().associate {
        it to registerBlock(
            "${it.asString()}_table_lamp",
            TableLampBlock(
                FabricBlockSettings.of(Material.REDSTONE_LAMP, it)
                    .hardness(0.3f)
                    .resistance(0.3f)
                    .sounds(BlockSoundGroup.WOOL)
                    .build()
            )
        )
    }

    val TRADING_STATION: TradingStationBlock = registerBlock("trading_station", TradingStationBlock())

    val STONE_TORCH_GROUND = registerBlockWithoutItem("stone_torch", StoneTorchBlock())
    val STONE_TORCH_WALL = registerBlockWithoutItem(
        "wall_stone_torch",
        StoneTorchBlock.Wall(
            Block.Settings.copy(STONE_TORCH_GROUND).dropsLike(STONE_TORCH_GROUND)
        )
    )

    fun init() {
        UseBlockCallback.EVENT.register(UseBlockCallback { player, world, hand, hitResult ->
            val state = world.getBlockState(hitResult.blockPos)
            val block = state.block
            // Check that:
            // - the block is a sneak-click handler
            // - the player is sneaking
            // - the player isn't holding an item (for block item and bucket support)
            if (block is SneakClickHandler && player.isSneaking && player.getStackInHand(hand).isEmpty) {
                block.onSneakClick(state, world, hitResult.blockPos, player, hand, hitResult)
            } else ActionResult.PASS
        })

        UseBlockCallback.EVENT.register(UseBlockCallback { player, world, hand, hitResult ->
            val pos = hitResult.blockPos.offset(hitResult.side)
            val state = world.getBlockState(pos)
            val block = state.block
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
                    ActionResult.SUCCESS
                }
            }

            ActionResult.PASS
        })
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        // BlockEntityRenderers
        BlockEntityRendererRegistry.INSTANCE.register(
            TradingStationBlock.BLOCK_ENTITY_TYPE,
            TradingStationRenderer(BlockEntityRenderDispatcher.INSTANCE)
        )
        BlockEntityRendererRegistry.INSTANCE.register(
            ShelfBlock.BLOCK_ENTITY_TYPE,
            ShelfRenderer(BlockEntityRenderDispatcher.INSTANCE)
        )

        // RenderLayers
        setRenderLayer(
            RenderLayer.getCutout(),
            TRADING_STATION, STONE_TORCH_GROUND, STONE_TORCH_WALL
        )
//        setRenderLayer(
//            RenderLayer.getTranslucent(),
//            OAK_KITCHEN_SINK,
//            SPRUCE_KITCHEN_SINK,
//            BIRCH_KITCHEN_SINK,
//            JUNGLE_KITCHEN_SINK,
//            ACACIA_KITCHEN_SINK,
//            DARK_OAK_KITCHEN_SINK
//        )

        // BlockColorProviders
        ColorProviderRegistry.BLOCK.register(
            SinkColorProvider,
            OAK_KITCHEN_SINK,
            SPRUCE_KITCHEN_SINK,
            BIRCH_KITCHEN_SINK,
            JUNGLE_KITCHEN_SINK,
            ACACIA_KITCHEN_SINK,
            DARK_OAK_KITCHEN_SINK
        )
    }

    @Environment(EnvType.CLIENT)
    private fun setRenderLayer(layer: RenderLayer, vararg blocks: Block) {
        for (block in blocks) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, layer)
        }
    }
}
