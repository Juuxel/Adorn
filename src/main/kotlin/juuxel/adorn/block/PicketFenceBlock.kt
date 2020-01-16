package juuxel.adorn.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Waterloggable
import net.minecraft.entity.EntityContext
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

class PicketFenceBlock(settings: Settings) : Block(settings), Waterloggable {
    init {
        defaultState = defaultState
            .with(FACING, Direction.NORTH)
            .with(SHAPE, Shape.Straight)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(SHAPE, FACING, WATERLOGGED)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return super.getPlacementState(ctx)?.let { state ->
            state.with(FACING, ctx.playerFacing.opposite)
                .with(SHAPE, Shape.Straight)
                .with(WATERLOGGED, ctx.world.getFluidState(ctx.blockPos).fluid === Fluids.WATER)
        }
    }

    override fun getOutlineShape(
        state: BlockState, view: BlockView, pos: BlockPos, ePos: EntityContext
    ): VoxelShape {
        // TODO: Outline shape
        return super.getOutlineShape(state, view, pos, ePos)
    }

    override fun getCollisionShape(
        state: BlockState, view: BlockView, pos: BlockPos, ePos: EntityContext
    ): VoxelShape {
        // TODO: Collision shape
        return super.getCollisionShape(state, view, pos, ePos)
    }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    companion object {
        val SHAPE: EnumProperty<Shape> = EnumProperty.of("shape", Shape::class.java)
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
    }

    enum class Shape(private val id: String) : StringIdentifiable {
        Straight("straight"),
        OuterCorner("outer_corner"),
        InnerCorner("inner_corner"),
        MismatchedCorner("mismatched_corner"),
        ;

        override fun asString() = id
    }
}
