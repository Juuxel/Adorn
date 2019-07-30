package juuxel.adorn.block

import juuxel.adorn.api.util.BlockVariant
import juuxel.polyester.block.PolyesterBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Waterloggable
import net.minecraft.entity.EntityContext
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

open class PostBlock(variant: BlockVariant) : Block(variant.createSettings()), PolyesterBlock, Waterloggable {
    override val name = "${variant.variantName}_post"
    override val itemSettings = Item.Settings().group(ItemGroup.DECORATIONS)
    override val hasDescription = true
    override val descriptionKey = "block.adorn.post.desc"

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
        PlatformBlock.POST_SHAPE
}
