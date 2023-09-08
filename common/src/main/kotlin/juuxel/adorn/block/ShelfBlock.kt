@file:Suppress("DEPRECATION")

package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.buildShapeRotations
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

class ShelfBlock(variant: BlockVariant) : VisibleBlockWithEntity(variant.createSettings()), Waterloggable, BlockWithDescription {
    override val descriptionKey = "block.adorn.shelf.description"

    init {
        defaultState = defaultState.with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, WATERLOGGED)
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) =
        SHAPES[state[FACING]]

    // Based on WallTorchBlock.canPlaceAt
    override fun canPlaceAt(state: BlockState, world: WorldView, pos: BlockPos): Boolean {
        val facing = state[FACING]
        val neighborPos = pos.offset(facing.opposite)
        return world.getBlockState(neighborPos).isSideSolidFullSquare(world, neighborPos, facing)
    }

    // Based on WallTorchBlock.getPlacementState
    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        val waterlogged = context.world.getFluidState(context.blockPos).fluid == Fluids.WATER
        context.placementDirections.asSequence()
            .filter { it.axis.isHorizontal }
            .map { it.opposite }
            .forEach {
                val state = defaultState.with(FACING, it).with(WATERLOGGED, waterlogged)
                if (state.canPlaceAt(context.world, context.blockPos)) {
                    return state
                }
            }

        return null
    }

    // Based on WallTorchBlock.getStateForNeighborUpdate
    override fun getStateForNeighborUpdate(
        state: BlockState, side: Direction, neighborState: BlockState, world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (state[WATERLOGGED]) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }

        return if (state[FACING].opposite == side && !state.canPlaceAt(world, pos)) {
            Blocks.AIR.defaultState
        } else {
            state
        }
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult
    ): ActionResult {
        val be = world.getBlockEntity(pos)
        if (be !is Inventory) return ActionResult.PASS

        val slot = getSlot(state, hitResult)
        val existing = be.getStack(slot)

        if (existing.isEmpty) {
            val handStack = player.getStackInHand(hand)

            if (!handStack.isEmpty) {
                val stack = handStack.copy()
                stack.count = 1
                be.setStack(slot, stack)
                be.markDirty()
                if (!world.isClient) {
                    PlatformBridges.network.syncBlockEntity(be)
                    player.incrementStat(AdornStats.INTERACT_WITH_SHELF)
                }

                if (!player.abilities.creativeMode) {
                    handStack.decrement(1)
                }
            }
        } else {
            if (!world.isClient) {
                if (player.getStackInHand(hand).isEmpty) {
                    player.setStackInHand(hand, existing)
                } else {
                    ItemScatterer.spawn(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), existing)
                }
            }
            be.setStack(slot, ItemStack.EMPTY)
            be.markDirty()
            if (!world.isClient) {
                PlatformBridges.network.syncBlockEntity(be)
                player.incrementStat(AdornStats.INTERACT_WITH_SHELF)
            }
        }

        return ActionResult.success(world.isClient)
    }

    /**
     * Returns the slot that the player hit or -1 if it's not available.
     */
    private fun getSlot(state: BlockState, hitResult: BlockHitResult): Int {
        val blockPos = hitResult.blockPos
        val pos = hitResult.pos
        val xo = pos.x - blockPos.x
        val zo = pos.z - blockPos.z

        val facing = state[FACING]

        return when (hitResult.side) {
            facing, Direction.UP, Direction.DOWN, facing.opposite -> when (facing) {
                Direction.EAST -> if (zo <= 0.5) 0 else 1
                Direction.WEST -> if (zo <= 0.5) 1 else 0
                Direction.NORTH -> if (xo <= 0.5) 0 else 1
                Direction.SOUTH -> if (xo <= 0.5) 1 else 0
                else -> -1
            }

            // Right side of shelf
            facing.rotateYCounterclockwise() -> 1

            // Left side of shelf
            facing.rotateYClockwise() -> 0

            else -> -1
        }
    }

    override fun getFluidState(state: BlockState) =
        if (state[Properties.WATERLOGGED]) {
            Fluids.WATER.getStill(false)
        } else {
            super.getFluidState(state)
        }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        if (!state.isOf(newState.block)) {
            val entity = world.getBlockEntity(pos)

            if (entity is Inventory) {
                ItemScatterer.spawn(world, pos, entity)
                world.updateComparators(pos, this)
            }

            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }

    override fun mirror(state: BlockState, mirror: BlockMirror) =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun rotate(state: BlockState, rotation: BlockRotation) =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        Menu.calculateComparatorOutput(world.getBlockEntity(pos))

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.SHELF.instantiate(pos, state)

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val WATERLOGGED = Properties.WATERLOGGED

        private val SHAPES = buildShapeRotations(0, 5, 0, 7, 6, 16)
    }
}
