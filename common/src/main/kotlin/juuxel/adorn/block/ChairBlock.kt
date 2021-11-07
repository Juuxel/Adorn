@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.util.buildShapeRotations
import juuxel.adorn.util.mergeIntoShapeMap
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.block.TallPlantBlock
import net.minecraft.block.Waterloggable
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class ChairBlock(variant: BlockVariant) : CarpetedBlock(variant.createSettings()), Waterloggable {
    override val sittingStat = AdornStats.SIT_ON_CHAIR

    init {
        defaultState = defaultState.with(HALF, DoubleBlockHalf.LOWER)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, HALF, WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        val pos = context.blockPos

        return if (pos.y < 255 && context.world.getBlockState(pos.up()).canReplace(context))
            super.getPlacementState(context)!!.with(FACING, context.playerFacing.opposite)
                .with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)
        else null
    }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        return if (state[HALF] != DoubleBlockHalf.UPPER) {
            super.canPlaceAt(state, world, pos)
        } else {
            val downState = world.getBlockState(pos.down())
            downState.block == this && downState[HALF] == DoubleBlockHalf.LOWER
        }
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) {
        if (!world.isClient && player.isCreative) {
            TallPlantBlock.onBreakInCreative(world, pos, state, player)
        }

        super.onBreak(world, pos, state, player)
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        world.setBlockState(
            pos.up(),
            defaultState.with(HALF, DoubleBlockHalf.UPPER)
                .with(FACING, state[FACING])
                .let { FluidUtil.updateFluidFromState(it, world.getFluidState(pos.up())) }
        )
    }

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: ShapeContext?) =
        if (state[HALF] == DoubleBlockHalf.LOWER) {
            if (isCarpetingEnabled() && state[CARPET].isPresent) {
                LOWER_SHAPES_WITH_CARPET[state[FACING]]
            } else {
                LOWER_SHAPES[state[FACING]]
            }
        } else UPPER_OUTLINE_SHAPES[state[FACING]]

    override fun getCollisionShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: ShapeContext?) =
        if (state[HALF] == DoubleBlockHalf.LOWER) {
            if (isCarpetingEnabled() && state[CARPET].isPresent) {
                LOWER_SHAPES_WITH_CARPET[state[FACING]]
            } else {
                LOWER_SHAPES[state[FACING]]
            }
        } else VoxelShapes.empty() // Let the bottom one handle the collision

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        val half = state[HALF]
        return if (
            // Updated from other half's direction vertically (LOWER + UP or UPPER + DOWN)
            direction.axis == Direction.Axis.Y && (half == DoubleBlockHalf.LOWER) == (direction == Direction.UP)
        ) {
            // If the other half is not a chair, break block
            if (neighborState.block != this)
                Blocks.AIR.defaultState
            else
                state.with(FACING, neighborState[FACING])
        } else {
            super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        }
    }

    override fun getActualSeatPos(world: World, state: BlockState, pos: BlockPos) =
        when (state[HALF]!!) {
            DoubleBlockHalf.UPPER -> pos.down()
            DoubleBlockHalf.LOWER -> pos
        }

    override fun mirror(state: BlockState, mirror: BlockMirror) =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun rotate(state: BlockState, rotation: BlockRotation) =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun canStateBeCarpeted(state: BlockState) =
        super.canStateBeCarpeted(state) && state[HALF] == DoubleBlockHalf.LOWER

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val HALF = Properties.DOUBLE_BLOCK_HALF
        val CARPET = CarpetedBlock.CARPET
        val WATERLOGGED = Properties.WATERLOGGED

        private val LOWER_SHAPES: Map<Direction, VoxelShape>
        private val LOWER_SHAPES_WITH_CARPET: Map<Direction, VoxelShape>
        private val UPPER_OUTLINE_SHAPES: Map<Direction, VoxelShape>

        init {
            val lowerSeatShape = VoxelShapes.union(
                createCuboidShape(2.0, 8.0, 2.0, 14.0, 10.0, 14.0),
                // Legs
                createCuboidShape(2.0, 0.0, 2.0, 4.0, 8.0, 4.0),
                createCuboidShape(12.0, 0.0, 2.0, 14.0, 8.0, 4.0),
                createCuboidShape(2.0, 0.0, 12.0, 4.0, 8.0, 14.0),
                createCuboidShape(12.0, 0.0, 12.0, 14.0, 8.0, 14.0)
            )
            val lowerBackShapes = buildShapeRotations(2, 10, 2, 4, 24, 14)
            LOWER_SHAPES = mergeIntoShapeMap(lowerBackShapes, lowerSeatShape)
            LOWER_SHAPES_WITH_CARPET = mergeIntoShapeMap(LOWER_SHAPES, CARPET_SHAPE)

            val upperSeatShape = VoxelShapes.union(
                createCuboidShape(2.0, -8.0, 2.0, 14.0, -6.0, 14.0),
                // Legs
                createCuboidShape(2.0, -16.0, 2.0, 4.0, -8.0, 4.0),
                createCuboidShape(12.0, -16.0, 2.0, 14.0, -8.0, 4.0),
                createCuboidShape(2.0, -16.0, 12.0, 4.0, -8.0, 14.0),
                createCuboidShape(12.0, -16.0, 12.0, 14.0, -8.0, 14.0)
            )
            val upperBackShapes = buildShapeRotations(2, -6, 2, 4, 8, 14)
            UPPER_OUTLINE_SHAPES = mergeIntoShapeMap(upperBackShapes, upperSeatShape)
        }
    }
}
