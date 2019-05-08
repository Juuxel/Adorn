package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import juuxel.adorn.util.shapeRotations
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

abstract class BaseKitchenCounterBlock : Block(
    FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).sounds(SOUND_GROUP).build()
), PolyesterBlock {
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite)

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        SHAPES[state[FACING]]

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
        private val TOP_SHAPE = createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0)
        val SHAPES = shapeRotations(
            0, 0, 0,
            14, 12, 16
        ).mapValues { (_, shape) ->
            VoxelShapes.union(shape, TOP_SHAPE)
        }
    }
}
