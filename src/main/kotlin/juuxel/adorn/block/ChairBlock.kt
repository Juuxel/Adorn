package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import virtuoel.towelette.api.Fluidloggable

class ChairBlock(material: String) : Block(Settings.copy(Blocks.OAK_FENCE)), PolyesterBlock, Fluidloggable {
    override val name = "${material}_chair"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.with(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite)

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?) =
        SEAT_SHAPE

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        private val SEAT_SHAPE = VoxelShapes.union(
            createCuboidShape(1.0, 8.0, 1.0, 15.0, 10.0, 15.0),
            // Legs
            createCuboidShape(2.0, 0.0, 2.0, 4.0, 8.0, 4.0),
            createCuboidShape(12.0, 0.0, 2.0, 14.0, 8.0, 4.0),
            createCuboidShape(2.0, 0.0, 12.0, 4.0, 8.0, 14.0),
            createCuboidShape(12.0, 0.0, 12.0, 14.0, 8.0, 14.0)
        )
    }
}
