package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

abstract class BaseKitchenCounterBlock : Block(Settings.copy(Blocks.CRAFTING_TABLE)), PolyesterBlock {
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.with(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite)

    override fun isFullBoundsCubeForCulling(state: BlockState?) = false
    override fun isSimpleFullBlock(state: BlockState?, view: BlockView?, pos: BlockPos?) = false

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
    }
}
