package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

abstract class BaseKitchenCounterBlock : Block(
    FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).sounds(SOUND_GROUP).build()
), PolyesterBlock {
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.with(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite)

    override fun isFullBoundsCubeForCulling(state: BlockState?) = false
    override fun isSimpleFullBlock(state: BlockState?, view: BlockView?, pos: BlockPos?) = false

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        val SOUND_GROUP = BlockSoundGroup(
            1.0F, 1.0F,
            SoundEvents.BLOCK_WOOD_BREAK,
            SoundEvents.BLOCK_STONE_STEP,
            SoundEvents.BLOCK_WOOD_PLACE,
            SoundEvents.BLOCK_WOOD_HIT,
            SoundEvents.BLOCK_STONE_FALL
        )
    }
}
