package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import juuxel.adorn.block.entity.CarpetedBlockEntity
import juuxel.adorn.util.shapeRotations
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.entity.EntityContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World
import virtuoel.towelette.api.FluidProperty
import virtuoel.towelette.api.Fluidloggable

class ChairBlock(material: String) : SeatBlock(true, Settings.copy(Blocks.OAK_FENCE)), PolyesterBlock, Fluidloggable {
    override val name = "${material}_chair"
    // null to skip registration
    override val itemSettings: Nothing? = null
    override val blockEntityType = CarpetedBlockEntity.BLOCK_ENTITY_TYPE

    init {
        defaultState = defaultState.with(HALF, DoubleBlockHalf.LOWER)
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        appendCarpetedProperty(builder)
        builder.add(FACING, HALF)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        val pos = context.blockPos

        return if (pos.y < 255 && context.world.getBlockState(pos.up()).canReplace(context))
            super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite)
        else null
    }

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
            world.setBlockState(otherPos, Blocks.AIR.defaultState, 35)
            world.playLevelEvent(player, 2001, otherPos, getRawIdFromState(otherState))
            if (!world.isClient && !player.isCreative) {
                dropStacks(state, world, pos, null, player, player.mainHandStack)
                dropStacks(otherState, world, otherPos, null, player, player.mainHandStack)
            }

            player.incrementStat(Stats.MINED.getOrCreateStat(this))
        } else if (otherState.isAir && half == DoubleBlockHalf.LOWER) {
            // Allow breaking and dropping old chairs
            if (!world.isClient && !player.isCreative) {
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
                .with(FluidProperty.FLUID, FluidProperty.FLUID.of(world.getFluidState(pos)))
        )
    }

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        if (state[HALF] == DoubleBlockHalf.LOWER) LOWER_SHAPES[state[FACING]]
        else UPPER_OUTLINE_SHAPES[state[FACING]]

    override fun getCollisionShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        if (state[HALF] == DoubleBlockHalf.LOWER) LOWER_SHAPES[state[FACING]]
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

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        val HALF = Properties.DOUBLE_BLOCK_HALF

        private val LOWER_SEAT_SHAPE = VoxelShapes.union(
            createCuboidShape(2.0, 8.0, 2.0, 14.0, 10.0, 14.0),
            // Legs
            createCuboidShape(2.0, 0.0, 2.0, 4.0, 8.0, 4.0),
            createCuboidShape(12.0, 0.0, 2.0, 14.0, 8.0, 4.0),
            createCuboidShape(2.0, 0.0, 12.0, 4.0, 8.0, 14.0),
            createCuboidShape(12.0, 0.0, 12.0, 14.0, 8.0, 14.0)
        )
        private val LOWER_BACK_SHAPES = shapeRotations(2, 10, 2, 4, 24, 14)
        private val LOWER_SHAPES = Direction.values().filter { it.horizontal != -1 }.map {
            it to VoxelShapes.union(LOWER_SEAT_SHAPE, LOWER_BACK_SHAPES[it])
        }.toMap()

        private val UPPER_SEAT_SHAPE = VoxelShapes.union(
            createCuboidShape(2.0, -8.0, 2.0, 14.0, -6.0, 14.0),
            // Legs
            createCuboidShape(2.0, -16.0, 2.0, 4.0, -8.0, 4.0),
            createCuboidShape(12.0, -16.0, 2.0, 14.0, -8.0, 4.0),
            createCuboidShape(2.0, -16.0, 12.0, 4.0, -8.0, 14.0),
            createCuboidShape(12.0, -16.0, 12.0, 14.0, -8.0, 14.0)
        )
        private val UPPER_BACK_SHAPES = shapeRotations(2, -6, 2, 4, 8, 14)
        private val UPPER_OUTLINE_SHAPES = Direction.values().filter { it.horizontal != -1 }.map {
            it to VoxelShapes.union(UPPER_SEAT_SHAPE, UPPER_BACK_SHAPES[it])
        }.toMap()
    }
}
