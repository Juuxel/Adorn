package juuxel.adorn.block

import juuxel.adorn.util.withBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Waterloggable
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.DyeItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class TableLampBlock(settings: Settings) : Block(settings), Waterloggable {
    init {
        defaultState = defaultState
            .with(LIT, true)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(LIT, WATERLOGGED)
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult
    ): ActionResult {
        val stack = player.getStackInHand(hand)
        val item = stack.item
        if (item is DyeItem) {
            world.setBlockState(pos, state.withBlock(AdornBlocks.TABLE_LAMPS[item.color]!!))
            world.playSound(player, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 0.6f)
        } else {
            val wasLit = state[LIT]
            world.setBlockState(pos, state.with(LIT, !wasLit))
            val pitch = if (wasLit) 0.5f else 0.6f
            world.playSound(player, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, pitch)
        }
        return ActionResult.SUCCESS
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)?.run {
            with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)
        }

    override fun getFluidState(state: BlockState) =
        if (state[Properties.WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, ePos: EntityContext)
        = SHAPE

    @Suppress("deprecation")
    override fun getLuminance(state: BlockState) =
        if (state[LIT]) 15 else super.getLuminance(state)

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        if (state[LIT]) 15 else 0

    companion object {
        private val SHAPE: VoxelShape = createCuboidShape(
            3.0, 0.0, 3.0,
            13.0, 14.0, 13.0
        )

        val WATERLOGGED: BooleanProperty = Properties.WATERLOGGED
        val LIT: BooleanProperty = Properties.LIT
    }
}
