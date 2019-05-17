package juuxel.adorn.block

import com.google.common.collect.Sets
import io.github.juuxel.polyester.block.PolyesterBlock
import juuxel.adorn.block.property.FrontConnection
import juuxel.adorn.util.shapeRotations
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateFactory
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.World
import virtuoel.towelette.api.Fluidloggable

class SofaBlock(variant: String) : SeatBlock(Settings.copy(Blocks.WHITE_WOOL)), PolyesterBlock, Fluidloggable, SneakClickHandler {
    override val name = "${variant}_sofa"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    init {
        defaultState = defaultState
            .with(FRONT_CONNECTION, FrontConnection.None)
            .with(CONNECTED_LEFT, false)
            .with(CONNECTED_RIGHT, false)
    }

    override fun onSneakClick(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity): ActionResult {
        val sleepingDirection = getSleepingDirection(world, pos)
        return if (world.dimension.canPlayersSleep() && sleepingDirection != null && !state[OCCUPIED]) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(OCCUPIED, true))
                val neighborPos = pos.offset(sleepingDirection)
                world.setBlockState(neighborPos, world.getBlockState(neighborPos).with(OCCUPIED, true))
                player.sleep(pos)
                (world as? ServerWorld)?.updatePlayersSleeping()
            }
            ActionResult.SUCCESS
        } else ActionResult.PASS
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, CONNECTED_LEFT, CONNECTED_RIGHT, FRONT_CONNECTION)
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
        val frontState = world.getBlockState(pos.offset(direction))

        val connectedLeft = leftState.block is SofaBlock && (leftState[FACING] == direction || (leftState[FACING] == direction.rotateYCounterclockwise() && leftState[FRONT_CONNECTION] != FrontConnection.None))
        val connectedRight = rightState.block is SofaBlock && (rightState[FACING] == direction || (rightState[FACING] == direction.rotateYClockwise() && rightState[FRONT_CONNECTION] != FrontConnection.None))
        val connectedFront = frontState.block is SofaBlock
        val connectedFrontLeft = connectedFront && !connectedLeft && frontState[FACING] == direction.rotateYCounterclockwise()
        val connectedFrontRight = connectedFront && !connectedRight && frontState[FACING] == direction.rotateYClockwise()
        val frontConnection = when {
            connectedFrontLeft -> FrontConnection.Left
            connectedFrontRight -> FrontConnection.Right
            else -> FrontConnection.None
        }

        return state
            .with(CONNECTED_LEFT, connectedLeft)
            .with(CONNECTED_RIGHT, connectedRight)
            .with(FRONT_CONNECTION, frontConnection)
    }

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        OUTLINE_SHAPE_MAP[SofaState(state)]

    override fun getCollisionShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        COLLISION_SHAPE_MAP[SofaState(state)]

    override fun mirror(state: BlockState, mirror: BlockMirror) =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun rotate(state: BlockState, rotation: BlockRotation) =
        state.with(FACING, rotation.rotate(state[FACING]))

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        val CONNECTED_LEFT = BooleanProperty.create("connected_left")
        val CONNECTED_RIGHT = BooleanProperty.create("connected_right")
        val FRONT_CONNECTION = EnumProperty.create("front", FrontConnection::class.java)

        private val BOTTOM = createCuboidShape(0.0, 2.0, 0.0, 16.0, 7.0, 16.0)
        private val LEFT_ARMS = shapeRotations(5, 7, 13, 16, 13, 16)
        private val RIGHT_ARMS = shapeRotations(5, 7, 0, 16, 13, 3)
        private val THIN_LEFT_ARMS = shapeRotations(5, 7, 14, 16, 13, 16)
        private val THIN_RIGHT_ARMS = shapeRotations(5, 7, 0, 16, 13, 2)
        private val BACKS = shapeRotations(0, 7, 0, 5, 16, 16)
        private val LEFT_CORNERS = shapeRotations(5, 7, 11, 16, 16, 16)
        private val RIGHT_CORNERS = shapeRotations(5, 7, 0, 16, 16, 5)

        private val BOOLEAN_SET = setOf(true, false)

        private val OUTLINE_SHAPE_MAP = buildShapeMap(thin = false)
        private val COLLISION_SHAPE_MAP = buildShapeMap(thin = true)

        private fun buildShapeMap(thin: Boolean): Map<SofaState, VoxelShape> =
            Sets.cartesianProduct(FACING.values.toSet(), BOOLEAN_SET, BOOLEAN_SET, FRONT_CONNECTION.values.toSet()).map {
                val facing = it[0] as Direction
                val left = it[1] as Boolean
                val right = it[2] as Boolean
                val front = it[3] as FrontConnection

                val parts = ArrayList<VoxelShape?>()
                parts += BACKS[facing]

                if (!left && front == FrontConnection.None) {
                    parts += if (thin) THIN_LEFT_ARMS[facing] else LEFT_ARMS[facing]
                }
                if (!right && front == FrontConnection.None) {
                    parts += if (thin) THIN_RIGHT_ARMS[facing] else RIGHT_ARMS[facing]
                }

                when (front) {
                    FrontConnection.Left -> parts += LEFT_CORNERS[facing]
                    FrontConnection.Right -> parts += RIGHT_CORNERS[facing]
                    else -> {}
                }

                SofaState(facing, left, right, front) to VoxelShapes.union(
                    BOTTOM,
                    *parts.filterNotNull().toTypedArray()
                )
            }.toMap()

        @JvmOverloads
        fun getSleepingDirection(world: World, pos: BlockPos, ignoreNeighbors: Boolean = false): Direction? {
            val state = world.getBlockState(pos)
            if (state.block !is SofaBlock) return null

            val connectedLeft = state[CONNECTED_LEFT]
            val connectedRight = state[CONNECTED_RIGHT]
            val frontConnection = state[FRONT_CONNECTION]
            val facing = state[FACING]

            if ((!connectedLeft && !connectedRight && frontConnection == FrontConnection.None) || (!ignoreNeighbors && state[OCCUPIED]))
                return null

            val result = when {
                frontConnection != FrontConnection.None -> facing
                connectedLeft -> facing.rotateYClockwise()
                connectedRight -> facing.rotateYCounterclockwise()
                else -> null
            }

            if (result != null) {
                if (ignoreNeighbors) {
                    return result
                }
                val neighborState = world.getBlockState(pos.offset(result))
                if (neighborState.block is SofaBlock && !neighborState[OCCUPIED]) {
                    return result
                }
            }

            return null
        }
    }

    private data class SofaState(val facing: Direction, val left: Boolean, val right: Boolean, val front: FrontConnection) {
        constructor(state: BlockState) : this(
            state[FACING],
            state[CONNECTED_LEFT],
            state[CONNECTED_RIGHT],
            state[FRONT_CONNECTION]
        )
    }
}
