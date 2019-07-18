package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import juuxel.adorn.util.buildShapeRotations
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.Waterloggable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.entity.EntityContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World
import java.util.*

class ChairBlock(material: String) : CarpetedBlock(Settings.copy(Blocks.OAK_FENCE)), PolyesterBlock, Waterloggable {
    override val name = "${material}_chair"
    // null to skip registration
    override val itemSettings: Nothing? = null

    init {
        defaultState = defaultState.with(HALF, DoubleBlockHalf.LOWER)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
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

    override fun canPlaceAt(state: BlockState, world: ViewableWorld, pos: BlockPos): Boolean {
        return if (state[HALF] != DoubleBlockHalf.UPPER) {
            super.canPlaceAt(state, world, pos)
        } else {
            val downState = world.getBlockState(pos.down())
            downState.block == this && downState[HALF] == DoubleBlockHalf.LOWER
        }
    }

    override fun afterBreak(
        world: World?, player: PlayerEntity?, pos: BlockPos?, state: BlockState?, be: BlockEntity?, stack: ItemStack?
    ) {
        super.afterBreak(world, player, pos, Blocks.AIR.defaultState, be, stack)
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) {
        val half = state[HALF]
        val otherPos = if (half == DoubleBlockHalf.LOWER) pos.up() else pos.down()
        val otherState = world.getBlockState(otherPos)

        // Check that the other block is the same and has the correct half, otherwise break
        if (otherState.block == this && otherState[HALF] != half) {
            world.setBlockState(otherPos, world.getFluidState(otherPos).blockState, 0b10_00_11)
            world.playLevelEvent(player, 2001, otherPos, getRawIdFromState(otherState))
            if (!player.isCreative) {
                dropStacks(state, world, pos, null, player, player.mainHandStack)
                dropStacks(otherState, world, otherPos, null, player, player.mainHandStack)
            }

            player.incrementStat(Stats.MINED.getOrCreateStat(this))
        } else if (otherState.isAir && half == DoubleBlockHalf.LOWER) {
            // Allow breaking and dropping old chairs
            if (!player.isCreative) {
                super.afterBreak(world, player, pos, state, null, player.mainHandStack)
            }
        }

        super.onBreak(world, pos, state, player)
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        world.setBlockState(
            pos.up(),
            defaultState.with(HALF, DoubleBlockHalf.UPPER)
                .with(FACING, state[FACING])
                .let { updateFluidPropertyOnPlaced(it, world.getFluidState(pos.up())) }
        )
    }

    /**
     * For using a mixin ([juuxel.adorn.mixin.fluidloggable.ChairBlockMixin]) to set the fluid property.
     */
    private fun updateFluidPropertyOnPlaced(state: BlockState, fluidState: FluidState) =
        state.with(WATERLOGGED, fluidState.fluid == Fluids.WATER)

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        if (state[HALF] == DoubleBlockHalf.LOWER) {
            if (state[CARPET].isPresent) LOWER_SHAPES_WITH_CARPET[state[FACING]]
            else LOWER_SHAPES[state[FACING]]
        }
        else UPPER_OUTLINE_SHAPES[state[FACING]]

    override fun getCollisionShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        if (state[HALF] == DoubleBlockHalf.LOWER) {
            if (state[CARPET].isPresent) LOWER_SHAPES_WITH_CARPET[state[FACING]]
            else LOWER_SHAPES[state[FACING]]
        }
        else VoxelShapes.empty() // Let the bottom one handle the collision

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: IWorld, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        val half = state[HALF]
        return if (
            // Updated from other half's direction vertically (LOWER + UP or UPPER + DOWN)
            // and vertical pair is invalid (either not a chair or the wrong half)
            direction.axis == Direction.Axis.Y &&
                (half == DoubleBlockHalf.LOWER) == (direction == Direction.UP) &&
                (neighborState.block != this || neighborState.get(HALF) == half)
        ) {
            Blocks.AIR.defaultState
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

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val HALF = Properties.DOUBLE_BLOCK_HALF
        val CARPET = CarpetedBlock.CARPET
        val WATERLOGGED = Properties.WATERLOGGED

        private val LOWER_SHAPES: EnumMap<Direction, VoxelShape>
        private val LOWER_SHAPES_WITH_CARPET: EnumMap<Direction, VoxelShape>
        private val UPPER_OUTLINE_SHAPES: EnumMap<Direction, VoxelShape>

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
            LOWER_SHAPES = EnumMap(
                Direction.values().filter { it.horizontal != -1 }.map {
                    it to VoxelShapes.union(
                        lowerSeatShape, lowerBackShapes[it]
                    )
                }.toMap()
            )

            LOWER_SHAPES_WITH_CARPET = EnumMap(
                LOWER_SHAPES.mapValues { (_, shape) ->
                    VoxelShapes.union(shape, CARPET_SHAPE)
                }
            )

            val upperSeatShape = VoxelShapes.union(
                createCuboidShape(2.0, -8.0, 2.0, 14.0, -6.0, 14.0),
                // Legs
                createCuboidShape(2.0, -16.0, 2.0, 4.0, -8.0, 4.0),
                createCuboidShape(12.0, -16.0, 2.0, 14.0, -8.0, 4.0),
                createCuboidShape(2.0, -16.0, 12.0, 4.0, -8.0, 14.0),
                createCuboidShape(12.0, -16.0, 12.0, 14.0, -8.0, 14.0)
            )
            val upperBackShapes = buildShapeRotations(2, -6, 2, 4, 8, 14)
            UPPER_OUTLINE_SHAPES = EnumMap(
                Direction.values().filter { it.horizontal != -1 }.map {
                    it to VoxelShapes.union(upperSeatShape, upperBackShapes[it])
                }.toMap()
            )
        }
    }
}
