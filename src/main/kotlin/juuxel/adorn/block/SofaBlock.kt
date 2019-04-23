package juuxel.adorn.block

import com.google.common.collect.Sets
import io.github.juuxel.polyester.registry.PolyesterBlock
import juuxel.adorn.util.shapeRotations
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
        OUTLINE_SHAPE_MAP[Triple(state.get(FACING), state.get(CONNECTED_LEFT), state.get(CONNECTED_RIGHT))]

    override fun getCollisionShape(state: BlockState, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?) =
        COLLISION_SHAPE_MAP[Triple(state.get(FACING), state.get(CONNECTED_LEFT), state.get(CONNECTED_RIGHT))]

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        val CONNECTED_LEFT = BooleanProperty.create("connected_left")
        val CONNECTED_RIGHT = BooleanProperty.create("connected_right")

        // TODO: Add arms to item model
        private val BOTTOM = createCuboidShape(0.0, 2.0, 0.0, 16.0, 7.0, 16.0)
        private val LEFT_ARMS = shapeRotations(5, 7, 13, 16, 13, 16)
        private val RIGHT_ARMS = shapeRotations(5, 7, 0, 16, 13, 3)
        private val THIN_LEFT_ARMS = shapeRotations(5, 7, 14, 16, 13, 16)
        private val THIN_RIGHT_ARMS = shapeRotations(5, 7, 0, 16, 13, 2)
        private val BACKS = shapeRotations(0, 7, 0, 5, 16, 16)

        private val OUTLINE_SHAPE_MAP = buildShapeMap(thin = false)
        private val COLLISION_SHAPE_MAP = buildShapeMap(thin = true)

        private fun buildShapeMap(thin: Boolean): Map<Triple<Direction, Boolean, Boolean>, VoxelShape> =
            Sets.cartesianProduct(FACING.values.toSet(), setOf(true, false), setOf(true, false)).map {
                val facing = it[0] as Direction
                val left = it[1] as Boolean
                val right = it[2] as Boolean

                val parts = ArrayList<VoxelShape?>()
                parts += BACKS[facing]

                if (!left) {
                    parts += if (thin) THIN_LEFT_ARMS[facing] else LEFT_ARMS[facing]
                }
                if (!right) {
                    parts += if (thin) THIN_RIGHT_ARMS[facing] else RIGHT_ARMS[facing]
                }

                Triple(facing, left, right) to VoxelShapes.union(
                    BOTTOM,
                    *parts.filterNotNull().toTypedArray()
                )
            }.toMap()
    }
}
