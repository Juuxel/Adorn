package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.property.FrontConnection
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess

open class KitchenCounterBlock(variant: BlockVariant) : AbstractKitchenCounterBlock(variant), BlockWithDescription {
    override val descriptionKey = "block.adorn.kitchen_counter.description"

    init {
        defaultState = defaultState.with(FRONT, FrontConnection.NONE)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FRONT)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState {
        return getStateForNeighborUpdate(
            super.getPlacementState(context),
            null, null, context.world, context.blockPos, null
        )
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction?, neighborState: BlockState?, world: WorldAccess, pos: BlockPos, neighborPos: BlockPos?
    ): BlockState {
        val facing = state[FACING]
        val frontState = world.getBlockState(pos.offset(facing))
        val frontConnection =
            if (frontState.block is AbstractKitchenCounterBlock) {
                when (frontState[FACING]) {
                    facing.rotateYClockwise() -> FrontConnection.LEFT
                    facing.rotateYCounterclockwise() -> FrontConnection.RIGHT
                    else -> FrontConnection.NONE
                }
            } else {
                FrontConnection.NONE
            }

        return state.with(FRONT, frontConnection)
    }

    companion object {
        val FRONT = EnumProperty.of("front", FrontConnection::class.java)
    }
}
