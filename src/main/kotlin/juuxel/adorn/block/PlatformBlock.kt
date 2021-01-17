@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.item.ItemWithDescription
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

open class PlatformBlock(variant: BlockVariant) : Block(variant.createSettings()), Waterloggable {
    init {
        defaultState = defaultState.with(Properties.WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
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

    override fun getOutlineShape(p0: BlockState?, p1: BlockView?, p2: BlockPos?, context: ShapeContext?): VoxelShape =
        COMBINED_SHAPE

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    @OnlyIn(Dist.CLIENT)
    override fun appendTooltip(
        stack: ItemStack, world: BlockView?, tooltip: MutableList<Text>, context: TooltipContext
    ) {
        tooltip += ItemWithDescription.createDescriptionText("block.adorn.platform.desc")
    }

    companion object {
        private val PLATFORM_SHAPE = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
        private val COMBINED_SHAPE = VoxelShapes.union(PostBlock.Y_SHAPE, PLATFORM_SHAPE)
    }
}
