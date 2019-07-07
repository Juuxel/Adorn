package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlockEntityType
import io.github.juuxel.polyester.block.PolyesterBlockWithEntity
import juuxel.adorn.api.util.BlockVariant
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.util.buildShapeRotations
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Waterloggable
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World

class ShelfBlock(variant: BlockVariant) : PolyesterBlockWithEntity(variant.createSettings()), Waterloggable {
    override val name = "${variant.variantName}_shelf"
    override val itemSettings = Item.Settings().group(ItemGroup.DECORATIONS)
    override val blockEntityType = BLOCK_ENTITY_TYPE

    init {
        defaultState = defaultState.with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, WATERLOGGED)
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: EntityContext) =
        SHAPES[state[FACING]]

    override fun getPlacementState(context: ItemPlacementContext): BlockState {
        val waterlogged = context.world.getFluidState(context.blockPos) == Fluids.WATER
        return defaultState.with(FACING, context.side).with(WATERLOGGED, waterlogged)
    }

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult
    ): Boolean {
        val be = world.getBlockEntity(pos) as? ShelfBlockEntity ?: return true
        val slot = getSlot(state, hitResult)
        val existing = be.getInvStack(slot)

        if (existing.isEmpty) {
            val handStack = player.getStackInHand(hand)

            if (!handStack.isEmpty) {
                val stack = handStack.copy()
                stack.count = 1
                be.setInvStack(slot, stack)
                be.markDirty()

                if (!player.abilities.creativeMode) {
                    handStack.decrement(1)
                }
            }
        } else {
            if (!world.isClient) {
                ItemScatterer.spawn(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), existing)
            }
            be.setInvStack(slot, ItemStack.EMPTY)
            be.markDirty()
        }

        return true
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
        if (state[Properties.WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun onBlockRemoved(state1: BlockState, world: World, pos: BlockPos, state2: BlockState, b: Boolean) {
        if (state1.block != state2.block) {
            val entity = world.getBlockEntity(pos)

            if (entity is BaseInventoryBlockEntity) {
                ItemScatterer.spawn(world, pos, entity)
                world.updateHorizontalAdjacent(pos, this)
            }

            super.onBlockRemoved(state1, world, pos, state2, b)
        }
    }

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val WATERLOGGED = Properties.WATERLOGGED
        val BLOCK_ENTITY_TYPE: BlockEntityType<ShelfBlockEntity> =
            PolyesterBlockEntityType(::ShelfBlockEntity)

        private val SHAPES = buildShapeRotations(0, 5, 0, 7, 6, 16)
    }
}
