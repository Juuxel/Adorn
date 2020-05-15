package juuxel.adorn.block

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.PaneBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

class ChainLinkFenceBlock(settings: Settings) : PaneBlock(settings) {
    init {
        defaultState = defaultState.with(UP, false).with(DOWN, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(UP, DOWN)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val state = super.getPlacementState(ctx) ?: return null
        val world = ctx.world
        val pos = ctx.blockPos

        return state
            .with(UP, connectsVerticallyTo(world.getBlockState(pos.up())))
            .with(DOWN, connectsVerticallyTo(world.getBlockState(pos.down())))
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, facing: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        var result = super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos)

        if (facing == Direction.UP) {
            result = result.with(UP, connectsVerticallyTo(neighborState))
        } else if (facing == Direction.DOWN) {
            result = result.with(DOWN, connectsVerticallyTo(neighborState))
        }

        return result
    }

    @Environment(EnvType.CLIENT)
    override fun isSideInvisible(state: BlockState, neighbor: BlockState, facing: Direction) = false

    private fun connectsVerticallyTo(state: BlockState) =
        state.block is ChainLinkFenceBlock

    companion object {
        val UP: BooleanProperty = Properties.UP
        val DOWN: BooleanProperty = Properties.DOWN
    }
}
