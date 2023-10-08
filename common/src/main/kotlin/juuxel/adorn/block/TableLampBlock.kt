@file:Suppress("DEPRECATION")

package juuxel.adorn.block

import juuxel.adorn.lib.AdornStats
import juuxel.adorn.util.buildShapeRotationsFromNorth
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class TableLampBlock(settings: Settings) : Block(settings), Waterloggable, BlockWithDescription {
    override val descriptionKey = "block.adorn.table_lamp.description"

    init {
        defaultState = defaultState
            .with(LIT, true)
            .with(WATERLOGGED, false)
            .with(FACING, Direction.UP)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(LIT, WATERLOGGED, FACING)
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult
    ): ActionResult {
        val stack = player.getStackInHand(hand)
        val item = stack.item
        if (item is DyeItem) {
            world.setBlockState(pos, AdornBlocks.TABLE_LAMPS[item.color]!!.getStateWithProperties(state))
            world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 0.8f)
            if (!player.abilities.creativeMode) stack.decrement(1)
            if (!world.isClient) player.incrementStat(AdornStats.DYE_TABLE_LAMP)
        } else {
            val wasLit = state[LIT]
            world.setBlockState(pos, state.with(LIT, !wasLit))
            val pitch = if (wasLit) 0.5f else 0.6f
            world.playSound(player, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, pitch)
            if (!world.isClient) player.incrementStat(AdornStats.INTERACT_WITH_TABLE_LAMP)
        }
        return ActionResult.SUCCESS
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState =
        defaultState
            .with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)
            .with(FACING, context.side)

    override fun getFluidState(state: BlockState): FluidState =
        if (state[WATERLOGGED]) {
            Fluids.WATER.getStill(false)
        } else {
            super.getFluidState(state)
        }

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, ePos: ShapeContext) =
        SHAPES[state[FACING]]

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        if (state[LIT]) 15 else 0

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState, world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state[WATERLOGGED]) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    companion object {
        private val SHAPES: Map<Direction, VoxelShape> = run {
            val shapeMap = buildShapeRotationsFromNorth(3, 3, 2, 13, 13, 16)

            shapeMap[Direction.UP] = createCuboidShape(
                3.0, 0.0, 3.0,
                13.0, 14.0, 13.0
            )

            shapeMap[Direction.DOWN] = createCuboidShape(
                3.0, 2.0, 3.0,
                13.0, 16.0, 13.0
            )

            shapeMap
        }

        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        val LIT: BooleanProperty = Properties.LIT
        val FACING: DirectionProperty = Properties.FACING

        fun createBlockSettings(color: DyeColor): Settings =
            Settings.create()
                .mapColor(color)
                .solid()
                .hardness(0.3f)
                .resistance(0.3f)
                .sounds(BlockSoundGroup.WOOL)
                .luminance { if (it[LIT]) 15 else 0 }
    }
}
