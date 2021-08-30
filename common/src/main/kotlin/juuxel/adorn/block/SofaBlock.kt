@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import com.google.common.collect.Sets
import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.property.FrontConnection
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.buildShapeRotations
import juuxel.adorn.util.withBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class SofaBlock(variant: BlockVariant) :
    SeatBlock(variant.createSettings()),
    Waterloggable,
    SneakClickHandler {
    init {
        defaultState = defaultState
            .with(FRONT_CONNECTION, FrontConnection.NONE)
            .with(CONNECTED_LEFT, false)
            .with(CONNECTED_RIGHT, false)
            .with(WATERLOGGED, false)
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult
    ): ActionResult {
        val stack = player.getStackInHand(hand)
        val item = stack.item
        if (item is DyeItem) {
            world.setBlockState(pos, state.withBlock(AdornBlocks.SOFAS[item.color]!!))
            world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 0.8f)
            if (!player.abilities.creativeMode) stack.decrement(1)
            return ActionResult.SUCCESS
        }

        return super.onUse(state, world, pos, player, hand, hit)
    }

    override fun onSneakClick(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult): ActionResult {
        val sleepingDirection = getSleepingDirection(world, pos)
        return if (world.dimension.isNatural && sleepingDirection != null && !state[OCCUPIED]) {
            if (world is ServerWorld) {
                with(PlatformBridges.entities) {
                    player.trySleep(pos) {
                        world.setBlockState(pos, state.with(OCCUPIED, true))
                        val neighborPos = pos.offset(sleepingDirection)
                        world.setBlockState(neighborPos, world.getBlockState(neighborPos).with(OCCUPIED, true))
                        world.updateSleepingPlayers()
                    }
                }
            }
            ActionResult.SUCCESS
        } else ActionResult.PASS
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, CONNECTED_LEFT, CONNECTED_RIGHT, FRONT_CONNECTION, WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState {
        return updateConnections(
            super.getPlacementState(context)!!.with(FACING, context.playerFacing.opposite)
                .with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER),
            context.world,
            context.blockPos
        )
    }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction?,
        neighborState: BlockState?,
        world: WorldAccess,
        pos: BlockPos,
        neighborPos: BlockPos?
    ): BlockState {
        return updateConnections(state, world, pos)
    }

    private fun updateConnections(state: BlockState, world: WorldAccess, pos: BlockPos): BlockState {
        val direction = state.get(FACING)
        val leftState = world.getBlockState(pos.offset(direction.rotateYClockwise()))
        val rightState = world.getBlockState(pos.offset(direction.rotateYCounterclockwise()))
        val frontState = world.getBlockState(pos.offset(direction))

        val connectedLeft = leftState.block is SofaBlock && (leftState[FACING] == direction || (leftState[FACING] == direction.rotateYCounterclockwise() && leftState[FRONT_CONNECTION] != FrontConnection.NONE))
        val connectedRight = rightState.block is SofaBlock && (rightState[FACING] == direction || (rightState[FACING] == direction.rotateYClockwise() && rightState[FRONT_CONNECTION] != FrontConnection.NONE))
        val connectedFront = frontState.block is SofaBlock
        val connectedFrontLeft = connectedFront && !connectedLeft && frontState[FACING] == direction.rotateYCounterclockwise()
        val connectedFrontRight = connectedFront && !connectedRight && frontState[FACING] == direction.rotateYClockwise()
        val frontConnection = when {
            connectedFrontLeft -> FrontConnection.LEFT
            connectedFrontRight -> FrontConnection.RIGHT
            else -> FrontConnection.NONE
        }

        return state
            .with(CONNECTED_LEFT, connectedLeft)
            .with(CONNECTED_RIGHT, connectedRight)
            .with(FRONT_CONNECTION, frontConnection)
    }

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: ShapeContext?) =
        OUTLINE_SHAPE_MAP[
            Bits.buildSofaState(
                state[FACING],
                state[CONNECTED_LEFT],
                state[CONNECTED_RIGHT],
                state[FRONT_CONNECTION]
            )
        ]

    override fun getCollisionShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: ShapeContext?) =
        COLLISION_SHAPE_MAP[
            Bits.buildSofaState(
                state[FACING],
                state[CONNECTED_LEFT],
                state[CONNECTED_RIGHT],
                state[FRONT_CONNECTION]
            )
        ]

    override fun mirror(state: BlockState, mirror: BlockMirror) =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun rotate(state: BlockState, rotation: BlockRotation) =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val CONNECTED_LEFT = BooleanProperty.of("connected_left")
        val CONNECTED_RIGHT = BooleanProperty.of("connected_right")
        val FRONT_CONNECTION = EnumProperty.of("front", FrontConnection::class.java)
        val WATERLOGGED = Properties.WATERLOGGED

        private val OUTLINE_SHAPE_MAP: Byte2ObjectMap<VoxelShape>
        private val COLLISION_SHAPE_MAP: Byte2ObjectMap<VoxelShape>

        init {
            val bottom = createCuboidShape(0.0, 2.0, 0.0, 16.0, 7.0, 16.0)
            val leftArms = buildShapeRotations(5, 7, 13, 16, 13, 16)
            val rightArms = buildShapeRotations(5, 7, 0, 16, 13, 3)
            val thinLeftArms = buildShapeRotations(5, 7, 14, 16, 13, 16)
            val thinRightArms = buildShapeRotations(5, 7, 0, 16, 13, 2)
            val backs = buildShapeRotations(0, 7, 0, 5, 16, 16)
            val leftCorners = buildShapeRotations(5, 7, 11, 16, 16, 16)
            val rightCorners = buildShapeRotations(5, 7, 0, 16, 16, 5)
            val booleans = setOf(true, false)

            fun buildShapeMap(thin: Boolean): Byte2ObjectMap<VoxelShape> = Byte2ObjectOpenHashMap(
                Sets.cartesianProduct<Any>(FACING.values.toSet(), booleans, booleans, FRONT_CONNECTION.values.toSet()).map {
                    val facing = it[0] as Direction
                    val left = it[1] as Boolean
                    val right = it[2] as Boolean
                    val front = it[3] as FrontConnection

                    val parts = ArrayList<VoxelShape?>()
                    parts += backs[facing]

                    if (!left && front == FrontConnection.NONE) {
                        parts += if (thin) thinLeftArms[facing] else leftArms[facing]
                    }
                    if (!right && front == FrontConnection.NONE) {
                        parts += if (thin) thinRightArms[facing] else rightArms[facing]
                    }

                    when (front) {
                        FrontConnection.LEFT -> parts += leftCorners[facing]
                        FrontConnection.RIGHT -> parts += rightCorners[facing]
                        else -> {}
                    }

                    Bits.buildSofaState(facing, left, right, front) to VoxelShapes.union(
                        bottom,
                        *parts.filterNotNull().toTypedArray()
                    )
                }.toMap()
            )

            OUTLINE_SHAPE_MAP = buildShapeMap(thin = false)
            COLLISION_SHAPE_MAP = buildShapeMap(thin = true)
        }

        @JvmOverloads
        @JvmStatic
        fun getSleepingDirection(world: World, pos: BlockPos, ignoreNeighbors: Boolean = false): Direction? {
            val state = world.getBlockState(pos)
            if (state.block !is SofaBlock) return null

            val connectedLeft = state[CONNECTED_LEFT]
            val connectedRight = state[CONNECTED_RIGHT]
            val frontConnection = state[FRONT_CONNECTION]
            val facing = state[FACING]

            if ((!connectedLeft && !connectedRight && frontConnection == FrontConnection.NONE) || (!ignoreNeighbors && state[OCCUPIED]))
                return null

            val result = when {
                frontConnection != FrontConnection.NONE -> facing
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
}
