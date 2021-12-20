package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.util.buildShapeRotations
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class KitchenSinkBlock(variant: BlockVariant) : KitchenCounterBlock(variant), BlockEntityProvider, Waterloggable {
    init {
        defaultState = defaultState.with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(WATERLOGGED)
    }

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext) =
        SHAPES[state[FACING]]

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        if (state[WATERLOGGED]) 15
        else 0

    // This is needed so that Towelette's default impl of getFluidState doesn't return water here.
    override fun getFluidState(state: BlockState?): FluidState =
        Fluids.EMPTY.defaultState

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    companion object {
        private val WATERLOGGED = Properties.WATERLOGGED
        private val SHAPES: Map<Direction, VoxelShape>

        init {
            val sinkShapes = buildShapeRotations(
                2, 7, 2,
                13, 16, 14
            )
            SHAPES = AbstractKitchenCounterBlock.SHAPES.mapValues { (direction, shape) ->
                VoxelShapes.combineAndSimplify(
                    shape,
                    sinkShapes[direction] ?: error(
                        "Error building kitchen sink shapes: couldn't find sink shape for direction '$direction'"
                    ),
                    BooleanBiFunction.ONLY_FIRST
                )
            }
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.KITCHEN_SINK.instantiate(pos, state)
}
