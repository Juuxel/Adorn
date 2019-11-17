package juuxel.adorn.block

import juuxel.adorn.util.buildShapeRotations
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityContext
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

abstract class BaseKitchenCounterBlock(
    settings: Settings = FabricBlockSettings.copy(Blocks.CRAFTING_TABLE).sounds(SOUND_GROUP).build()
) : Block(settings) {
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerFacing.opposite)

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        SHAPES[state[FACING]]

    override fun mirror(state: BlockState, mirror: BlockMirror) =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun rotate(state: BlockState, rotation: BlockRotation) =
        state.with(FACING, rotation.rotate(state[FACING]))

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val SOUND_GROUP = BlockSoundGroup(
            1.0F, 1.0F,
            SoundEvents.BLOCK_WOOD_BREAK,
            SoundEvents.BLOCK_STONE_STEP,
            SoundEvents.BLOCK_WOOD_PLACE,
            SoundEvents.BLOCK_WOOD_HIT,
            SoundEvents.BLOCK_STONE_FALL
        )
        private val TOP_SHAPE = createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0)
        val SHAPES = buildShapeRotations(
            0, 0, 0,
            14, 12, 16
        ).mapValues { (_, shape) ->
            VoxelShapes.union(shape, TOP_SHAPE)
        }
    }
}
