package juuxel.adorn.block

import juuxel.adorn.util.withBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.Waterloggable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.entity.EntityContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.WorldView

class TallLampBlock(settings: Settings) : Block(settings), Waterloggable {
    init {
        defaultState = defaultState
            .with(HALF, DoubleBlockHalf.LOWER)
            .with(LIT, true)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(HALF, LIT, WATERLOGGED)
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult
    ): ActionResult {
        val stack = player.getStackInHand(hand)
        val item = stack.item
        if (item is DyeItem && state[HALF] == DoubleBlockHalf.UPPER) {
            world.setBlockState(pos, state.withBlock(AdornBlocks.TALL_LAMPS[item.color]!!))
            world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 0.8f)
            if (!player.abilities.creativeMode) stack.decrement(1)
        } else {
            val wasLit = state[LIT]
            world.setBlockState(pos, state.with(LIT, !wasLit))
            val otherPos = getOtherHalf(pos, state)
            val otherState = world.getBlockState(otherPos)
            world.setBlockState(otherPos, otherState.with(LIT, !wasLit))
            val pitch = if (wasLit) 0.5f else 0.6f
            world.playSound(player, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, pitch)
        }
        return ActionResult.SUCCESS
    }

    private fun getOtherHalf(pos: BlockPos, state: BlockState): BlockPos =
        if (state[HALF] == DoubleBlockHalf.UPPER) pos.down()
        else pos.up()

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)?.run {
            with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)
        }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, ePos: EntityContext) =
        if (state[HALF] == DoubleBlockHalf.UPPER) TOP_SHAPE
        else BOTTOM_SHAPE

    @Suppress("deprecation")
    override fun getLuminance(state: BlockState) =
        if (state[LIT] && state[HALF] == DoubleBlockHalf.UPPER) 15 else super.getLuminance(state)

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        if (state[LIT]) 15 else 0

    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
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
        // TODO: Combine this and ChairBlock.onBreak
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
                .let { FluidUtil.updateFluidFromState(it, world.getFluidState(pos.up())) }
        )
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: IWorld, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        // Updated from other half's direction vertically (LOWER + UP or UPPER + DOWN)
        val half = state[HALF]
        val isY = direction.axis == Direction.Axis.Y
        val verticalMatch = (half == DoubleBlockHalf.LOWER) == (direction == Direction.UP)
        val shouldBreak = neighborState.block !== this
        return if (isY && verticalMatch && shouldBreak) {
            // If the other half is not a lamp, break block
            Blocks.AIR.defaultState
        } else {
            super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        }
    }

    companion object {
        val HALF: EnumProperty<DoubleBlockHalf> = Properties.DOUBLE_BLOCK_HALF
        val LIT: BooleanProperty = Properties.LIT
        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED

        private val TOP_SHAPE: VoxelShape = createCuboidShape(
            2.0, 0.0, 2.0,
            14.0, 14.0, 14.0
        )
        private val BOTTOM_SHAPE: VoxelShape = createCuboidShape(
            2.0, 0.0, 2.0,
            14.0, 16.0, 14.0
        )
    }
}
