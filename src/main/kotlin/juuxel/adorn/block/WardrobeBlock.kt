package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.MutableBlockEntityType
import juuxel.adorn.block.entity.WardrobeBlockEntity
import juuxel.adorn.gui.AdornGuis
import juuxel.adorn.gui.openFabricContainer
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.enums.DoubleBlockHalf
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.stat.Stats
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.IWorld
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World

class WardrobeBlock(variant: BlockVariant) : VisibleBlockWithEntity(variant.createSettings()), InventoryProvider {
    override val blockEntityType = BLOCK_ENTITY_TYPE

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, HALF)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        val pos = context.blockPos

        return if (pos.y < 255 && context.world.getBlockState(pos.up()).canReplace(context))
            super.getPlacementState(context)!!.with(FACING, context.playerFacing.opposite)
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

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        player.openFabricContainer(AdornGuis.WARDROBE, pos)
        return true
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState, player: PlayerEntity) {
        val half = state[HALF]
        val otherPos = if (half == DoubleBlockHalf.LOWER) pos.up() else pos.down()
        val otherState = world.getBlockState(otherPos)

        // Check that the other block is the same and has the correct half, otherwise break
        if (otherState.block == this && otherState[HALF] != half) {
            world.setBlockState(otherPos, world.getFluidState(otherPos).blockState, 0b10_00_11)
            world.playLevelEvent(player, 2001, otherPos, Block.getRawIdFromState(otherState))
            if (!player.isCreative) {
                Block.dropStacks(state, world, pos, null, player, player.mainHandStack)
                Block.dropStacks(otherState, world, otherPos, null, player, player.mainHandStack)
            }

            player.incrementStat(Stats.MINED.getOrCreateStat(this))
        }

        super.onBreak(world, pos, state, player)
    }

    override fun onBlockRemoved(state1: BlockState, world: World, pos: BlockPos, state2: BlockState, b: Boolean) {
        if (state1.block != state2.block) {
            val entity = world.getBlockEntity(pos)

            if (entity is BaseInventoryBlockEntity && state1[HALF] == DoubleBlockHalf.LOWER) {
                ItemScatterer.spawn(world, pos, getInventory(state1, world, pos))
                world.updateHorizontalAdjacent(pos, this)
            }

            super.onBlockRemoved(state1, world, pos, state2, b)
        }
    }

    // TODO: Removable?
    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: IWorld, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        val half = state[HALF]
        return if (
        // Updated from other half's direction vertically (LOWER + UP or UPPER + DOWN)
            direction.axis == Direction.Axis.Y && (half == DoubleBlockHalf.LOWER) == (direction == Direction.UP)
        ) {
            // If the other half is not a wardrobe, break block
            if (neighborState.block != this)
                Blocks.AIR.defaultState
            else
                state.with(FACING, neighborState[FACING])
        } else {
            super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        }
    }

    override fun mirror(state: BlockState, mirror: BlockMirror) =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun rotate(state: BlockState, rotation: BlockRotation) =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (stack.hasCustomName()) {
            (world.getBlockEntity(pos) as? WardrobeBlockEntity)?.customName = stack.name
        }

        world.setBlockState(
            pos.up(),
            defaultState.with(HALF, DoubleBlockHalf.UPPER).with(FACING, state[FACING])
        )
    }

    override fun getInventory(state: BlockState, world: IWorld, pos: BlockPos) =
        if (state[HALF] == DoubleBlockHalf.LOWER)
            (world.getBlockEntity(pos) as? BaseInventoryBlockEntity)?.sidedInventory
        else
            (world.getBlockEntity(pos.down()) as? BaseInventoryBlockEntity)?.sidedInventory

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val HALF = Properties.DOUBLE_BLOCK_HALF

        val BLOCK_ENTITY_TYPE: BlockEntityType<WardrobeBlockEntity> =
            MutableBlockEntityType(::WardrobeBlockEntity)
    }
}
