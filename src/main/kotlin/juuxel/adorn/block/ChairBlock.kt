package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import juuxel.adorn.util.shapeRotations
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import virtuoel.towelette.api.Fluidloggable

class ChairBlock(material: String) : Block(Settings.copy(Blocks.OAK_FENCE)), PolyesterBlock, Fluidloggable {
    override val name = "${material}_chair"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite)

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        SHAPES[state[FACING]]

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        private val SEAT_SHAPE = VoxelShapes.union(
            createCuboidShape(2.0, 8.0, 2.0, 14.0, 10.0, 14.0),
            // Legs
            createCuboidShape(2.0, 0.0, 2.0, 4.0, 8.0, 4.0),
            createCuboidShape(12.0, 0.0, 2.0, 14.0, 8.0, 4.0),
            createCuboidShape(2.0, 0.0, 12.0, 4.0, 8.0, 14.0),
            createCuboidShape(12.0, 0.0, 12.0, 14.0, 8.0, 14.0)
        )
        private val BACK_SHAPES = shapeRotations(2, 10, 2, 4, 24, 14)
        private val SHAPES = Direction.values().filter { it.horizontal != -1 }.map {
            it to VoxelShapes.union(SEAT_SHAPE, BACK_SHAPES[it])
        }.toMap()
    }
}
