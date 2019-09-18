package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Waterloggable
import net.minecraft.entity.EntityContext
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

open class PlatformBlock(variant: BlockVariant) : Block(variant.createSettings()), BlockWithDescription, Waterloggable {
    override val descriptionKey = "block.adorn.platform.desc"

    init {
        defaultState = defaultState.with(Properties.WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(Properties.WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)?.run {
            with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)
        }

    override fun getFluidState(state: BlockState) =
        if (state[Properties.WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun getOutlineShape(p0: BlockState?, p1: BlockView?, p2: BlockPos?, context: EntityContext?): VoxelShape =
        COMBINED_SHAPE

    companion object {
        internal val POST_SHAPE = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
        private val PLATFORM_SHAPE = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
        private val COMBINED_SHAPE = VoxelShapes.union(POST_SHAPE, PLATFORM_SHAPE)
    }
}
