package juuxel.adorn.block

import juuxel.adorn.Adorn
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.block.renderer.ShelfRenderer
import juuxel.adorn.block.renderer.TradingStationRenderer
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.lib.RegistryHelper
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.Block
import net.minecraft.block.CarpetBlock
import net.minecraft.item.BlockItem
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult

object AdornBlocks : RegistryHelper(Adorn.NAMESPACE) {
    val SOFAS: List<SofaBlock> = BlockVariant.WOOLS.values.map {
        // This is one place where the BlockVariant mapping is kept.
        // I will not write out sixteen sofa registrations.
        registerBlock(it.variantName + "_sofa", SofaBlock(it))
    }

    @JvmField val OAK_CHAIR: Block = registerBlock("oak_chair", ChairBlock(BlockVariant.OAK), ::ChairBlockItem)
    @JvmField val SPRUCE_CHAIR: Block = registerBlock("spruce_chair", ChairBlock(BlockVariant.SPRUCE), ::ChairBlockItem)
    @JvmField val BIRCH_CHAIR: Block = registerBlock("birch_chair", ChairBlock(BlockVariant.BIRCH), ::ChairBlockItem)
    @JvmField val JUNGLE_CHAIR: Block = registerBlock("jungle_chair", ChairBlock(BlockVariant.JUNGLE), ::ChairBlockItem)
    @JvmField val ACACIA_CHAIR: Block = registerBlock("acacia_chair", ChairBlock(BlockVariant.ACACIA), ::ChairBlockItem)
    @JvmField val DARK_OAK_CHAIR: Block = registerBlock("dark_oak_chair", ChairBlock(BlockVariant.DARK_OAK), ::ChairBlockItem)

    @JvmField val OAK_TABLE: Block = registerBlock("oak_table", TableBlock(BlockVariant.OAK), ::TableBlockItem)
    @JvmField val SPRUCE_TABLE: Block = registerBlock("spruce_table", TableBlock(BlockVariant.SPRUCE), ::TableBlockItem)
    @JvmField val BIRCH_TABLE: Block = registerBlock("birch_table", TableBlock(BlockVariant.BIRCH), ::TableBlockItem)
    @JvmField val JUNGLE_TABLE: Block = registerBlock("jungle_table", TableBlock(BlockVariant.JUNGLE), ::TableBlockItem)
    @JvmField val ACACIA_TABLE: Block = registerBlock("acacia_table", TableBlock(BlockVariant.ACACIA), ::TableBlockItem)
    @JvmField val DARK_OAK_TABLE: Block = registerBlock("dark_oak_table", TableBlock(BlockVariant.DARK_OAK), ::TableBlockItem)

    @JvmField val OAK_KITCHEN_COUNTER: Block = registerBlock("oak_kitchen_counter", KitchenCounterBlock())
    @JvmField val SPRUCE_KITCHEN_COUNTER: Block = registerBlock("spruce_kitchen_counter", KitchenCounterBlock())
    @JvmField val BIRCH_KITCHEN_COUNTER: Block = registerBlock("birch_kitchen_counter", KitchenCounterBlock())
    @JvmField val JUNGLE_KITCHEN_COUNTER: Block = registerBlock("jungle_kitchen_counter", KitchenCounterBlock())
    @JvmField val ACACIA_KITCHEN_COUNTER: Block = registerBlock("acacia_kitchen_counter", KitchenCounterBlock())
    @JvmField val DARK_OAK_KITCHEN_COUNTER: Block = registerBlock("dark_oak_kitchen_counter", KitchenCounterBlock())

    @JvmField val OAK_KITCHEN_CUPBOARD: Block = registerBlock("oak_kitchen_cupboard", KitchenCupboardBlock())
    @JvmField val SPRUCE_KITCHEN_CUPBOARD: Block = registerBlock("spruce_kitchen_cupboard", KitchenCupboardBlock())
    @JvmField val BIRCH_KITCHEN_CUPBOARD: Block = registerBlock("birch_kitchen_cupboard", KitchenCupboardBlock())
    @JvmField val JUNGLE_KITCHEN_CUPBOARD: Block = registerBlock("jungle_kitchen_cupboard", KitchenCupboardBlock())
    @JvmField val ACACIA_KITCHEN_CUPBOARD: Block = registerBlock("acacia_kitchen_cupboard", KitchenCupboardBlock())
    @JvmField val DARK_OAK_KITCHEN_CUPBOARD: Block = registerBlock("dark_oak_kitchen_cupboard", KitchenCupboardBlock())

    @JvmField val OAK_DRAWER: Block = registerBlock("oak_drawer", DrawerBlock(BlockVariant.OAK))
    @JvmField val SPRUCE_DRAWER: Block = registerBlock("spruce_drawer", DrawerBlock(BlockVariant.SPRUCE))
    @JvmField val BIRCH_DRAWER: Block = registerBlock("birch_drawer", DrawerBlock(BlockVariant.BIRCH))
    @JvmField val JUNGLE_DRAWER: Block = registerBlock("jungle_drawer", DrawerBlock(BlockVariant.JUNGLE))
    @JvmField val ACACIA_DRAWER: Block = registerBlock("acacia_drawer", DrawerBlock(BlockVariant.ACACIA))
    @JvmField val DARK_OAK_DRAWER: Block = registerBlock("dark_oak_drawer", DrawerBlock(BlockVariant.DARK_OAK))

    @JvmField val OAK_POST: Block = registerBlock("oak_post", PostBlock(BlockVariant.OAK))
    @JvmField val SPRUCE_POST: Block = registerBlock("spruce_post", PostBlock(BlockVariant.SPRUCE))
    @JvmField val BIRCH_POST: Block = registerBlock("birch_post", PostBlock(BlockVariant.BIRCH))
    @JvmField val JUNGLE_POST: Block = registerBlock("jungle_post", PostBlock(BlockVariant.JUNGLE))
    @JvmField val ACACIA_POST: Block = registerBlock("acacia_post", PostBlock(BlockVariant.ACACIA))
    @JvmField val DARK_OAK_POST: Block = registerBlock("dark_oak_post", PostBlock(BlockVariant.DARK_OAK))
    @JvmField val STONE_POST: Block = registerBlock("stone_post", PostBlock(BlockVariant.STONE))
    @JvmField val COBBLESTONE_POST: Block = registerBlock("cobblestone_post", PostBlock(BlockVariant.COBBLESTONE))
    @JvmField val SANDSTONE_POST: Block = registerBlock("sandstone_post", PostBlock(BlockVariant.SANDSTONE))
    @JvmField val DIORITE_POST: Block = registerBlock("diorite_post", PostBlock(BlockVariant.DIORITE))
    @JvmField val ANDESITE_POST: Block = registerBlock("andesite_post", PostBlock(BlockVariant.ANDESITE))
    @JvmField val GRANITE_POST: Block = registerBlock("granite_post", PostBlock(BlockVariant.GRANITE))

    @JvmField val OAK_PLATFORM: Block = registerBlock("oak_platform", PlatformBlock(BlockVariant.OAK))
    @JvmField val SPRUCE_PLATFORM: Block = registerBlock("spruce_platform", PlatformBlock(BlockVariant.SPRUCE))
    @JvmField val BIRCH_PLATFORM: Block = registerBlock("birch_platform", PlatformBlock(BlockVariant.BIRCH))
    @JvmField val JUNGLE_PLATFORM: Block = registerBlock("jungle_platform", PlatformBlock(BlockVariant.JUNGLE))
    @JvmField val ACACIA_PLATFORM: Block = registerBlock("acacia_platform", PlatformBlock(BlockVariant.ACACIA))
    @JvmField val DARK_OAK_PLATFORM: Block = registerBlock("dark_oak_platform", PlatformBlock(BlockVariant.DARK_OAK))
    @JvmField val STONE_PLATFORM: Block = registerBlock("stone_platform", PlatformBlock(BlockVariant.STONE))
    @JvmField val COBBLESTONE_PLATFORM: Block = registerBlock("cobblestone_platform", PlatformBlock(BlockVariant.COBBLESTONE))
    @JvmField val SANDSTONE_PLATFORM: Block = registerBlock("sandstone_platform", PlatformBlock(BlockVariant.SANDSTONE))
    @JvmField val DIORITE_PLATFORM: Block = registerBlock("diorite_platform", PlatformBlock(BlockVariant.DIORITE))
    @JvmField val ANDESITE_PLATFORM: Block = registerBlock("andesite_platform", PlatformBlock(BlockVariant.ANDESITE))
    @JvmField val GRANITE_PLATFORM: Block = registerBlock("granite_platform", PlatformBlock(BlockVariant.GRANITE))

    @JvmField val OAK_STEP: Block = registerBlock("oak_step", StepBlock(BlockVariant.OAK))
    @JvmField val SPRUCE_STEP: Block = registerBlock("spruce_step", StepBlock(BlockVariant.SPRUCE))
    @JvmField val BIRCH_STEP: Block = registerBlock("birch_step", StepBlock(BlockVariant.BIRCH))
    @JvmField val JUNGLE_STEP: Block = registerBlock("jungle_step", StepBlock(BlockVariant.JUNGLE))
    @JvmField val ACACIA_STEP: Block = registerBlock("acacia_step", StepBlock(BlockVariant.ACACIA))
    @JvmField val DARK_OAK_STEP: Block = registerBlock("dark_oak_step", StepBlock(BlockVariant.DARK_OAK))
    @JvmField val STONE_STEP: Block = registerBlock("stone_step", StepBlock(BlockVariant.STONE))
    @JvmField val COBBLESTONE_STEP: Block = registerBlock("cobblestone_step", StepBlock(BlockVariant.COBBLESTONE))
    @JvmField val SANDSTONE_STEP: Block = registerBlock("sandstone_step", StepBlock(BlockVariant.SANDSTONE))
    @JvmField val DIORITE_STEP: Block = registerBlock("diorite_step", StepBlock(BlockVariant.DIORITE))
    @JvmField val ANDESITE_STEP: Block = registerBlock("andesite_step", StepBlock(BlockVariant.ANDESITE))
    @JvmField val GRANITE_STEP: Block = registerBlock("granite_step", StepBlock(BlockVariant.GRANITE))

    @JvmField val OAK_SHELF: ShelfBlock = registerBlock("oak_shelf", ShelfBlock(BlockVariant.OAK))
    @JvmField val SPRUCE_SHELF: ShelfBlock = registerBlock("spruce_shelf", ShelfBlock(BlockVariant.SPRUCE))
    @JvmField val BIRCH_SHELF: ShelfBlock = registerBlock("birch_shelf", ShelfBlock(BlockVariant.BIRCH))
    @JvmField val JUNGLE_SHELF: ShelfBlock = registerBlock("jungle_shelf", ShelfBlock(BlockVariant.JUNGLE))
    @JvmField val ACACIA_SHELF: ShelfBlock = registerBlock("acacia_shelf", ShelfBlock(BlockVariant.ACACIA))
    @JvmField val DARK_OAK_SHELF: ShelfBlock = registerBlock("dark_oak_shelf", ShelfBlock(BlockVariant.DARK_OAK))
    val IRON_SHELF: ShelfBlock = registerBlock("iron_shelf", ShelfBlock(BlockVariant.IRON))

    @JvmField val BRICK_CHIMNEY: Block = registerBlock("brick_chimney", ChimneyBlock())
    @JvmField val STONE_BRICK_CHIMNEY: Block = registerBlock("stone_brick_chimney", ChimneyBlock())
    @JvmField val NETHER_BRICK_CHIMNEY: Block = registerBlock("nether_brick_chimney", ChimneyBlock())
    @JvmField val RED_NETHER_BRICK_CHIMNEY: Block = registerBlock("red_nether_brick_chimney", ChimneyBlock())
    @JvmField val COBBLESTONE_CHIMNEY: Block = registerBlock("cobblestone_chimney", ChimneyBlock())
    @JvmField val PRISMARINE_CHIMNEY: Block = registerBlock("prismarine_chimney", PrismarineChimneyBlock())

    @JvmField val TRADING_STATION: TradingStationBlock = registerBlock("trading_station", TradingStationBlock())

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
                    world.setBlockState(pos, state.with(CarpetedBlock.CARPET, CarpetedBlock.CARPET.wrapOrNone(carpet.color)))
                    val soundGroup = carpet.defaultState.soundGroup
                    world.playSound(player, pos, soundGroup.placeSound, SoundCategory.BLOCKS, (soundGroup.volume + 1f) / 2f, soundGroup.pitch * 0.8f)
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
        BlockEntityRendererRegistry.INSTANCE.register(
            TradingStationBlockEntity::class.java,
            TradingStationRenderer()
        )
        BlockEntityRendererRegistry.INSTANCE.register(
            ShelfBlockEntity::class.java,
            ShelfRenderer()
        )
    }
}
