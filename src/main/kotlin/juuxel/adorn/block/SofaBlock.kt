package juuxel.adorn.block

import com.google.common.collect.Sets
import io.github.juuxel.polyester.registry.PolyesterBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import virtuoel.towelette.api.Fluidloggable

class SofaBlock(variant: String) : Block(Settings.copy(Blocks.WHITE_WOOL)), PolyesterBlock, Fluidloggable {
    override val name = "${variant}_sofa"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.with(FACING, CONNECTED_LEFT, CONNECTED_RIGHT)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState {
        return updateConnections(
            super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite),
            context.world,
            context.blockPos
        )
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: IWorld,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState {
        return updateConnections(state, world, pos)
    }

    private fun updateConnections(state: BlockState, world: IWorld, pos: BlockPos): BlockState {
        val direction = state.get(FACING)
        val leftState = world.getBlockState(pos.offset(direction.rotateYClockwise()))
        val rightState = world.getBlockState(pos.offset(direction.rotateYCounterclockwise()))

        return state
            .with(CONNECTED_LEFT, leftState.block is SofaBlock && leftState[FACING] == direction)
            .with(CONNECTED_RIGHT, rightState.block is SofaBlock && rightState[FACING] == direction)
    }

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?) =
        SHAPE_MAP[Triple(state.get(FACING), state.get(CONNECTED_LEFT), state.get(CONNECTED_RIGHT))]

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        val CONNECTED_LEFT = BooleanProperty.create("connected_left")
        val CONNECTED_RIGHT = BooleanProperty.create("connected_right")

        private val BOTTOM = createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0)
        private val LEFT_ARMS = mapOf(
            Direction.EAST to box(4, 7, 14, 14, 13, 16),
            Direction.WEST to box(2, 7, 0, 12, 13, 2),

            Direction.SOUTH to box(0, 7, 4, 2, 13, 14),
            Direction.NORTH to box(14, 7, 2, 16, 13, 12)
        )
        private val RIGHT_ARMS = mapOf(
            Direction.EAST to box(4, 7, 0, 14, 13, 2),
            Direction.WEST to box(2, 7, 14, 12, 13, 16),

            Direction.SOUTH to box(14, 7, 4, 16, 13, 14),
            Direction.NORTH to box(0, 7, 2, 2, 13, 12)
        )

        private val SHAPE_MAP: Map<Triple<Direction, Boolean, Boolean>, VoxelShape> =
            Sets.cartesianProduct(FACING.values.toSet(), setOf(true, false), setOf(true, false)).map {
                val facing = it[0] as Direction
                val left = it[1] as Boolean
                val right = it[2] as Boolean

                val arms = ArrayList<VoxelShape?>()

                if (!left) {
                    arms += LEFT_ARMS[facing]
                }
                if (!right) {
                    arms += RIGHT_ARMS[facing]
                }

                Triple(facing, left, right) to VoxelShapes.union(
                    BOTTOM,
                    *arms.filterNotNull().toTypedArray()
                )
            }.toMap()
        
        private fun box(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int) =
            createCuboidShape(
                x1.toDouble(), y1.toDouble(), z1.toDouble(),
                x2.toDouble(), y2.toDouble(), z2.toDouble()
            )
    }
}
