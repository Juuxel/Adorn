package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import virtuoel.towelette.api.Fluidloggable

// TODO: Connections
class TableBlock(material: String) : Block(Settings.copy(Blocks.CRAFTING_TABLE)), PolyesterBlock, Fluidloggable {
    override val name = "${material}_table"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?) =
        SHAPE

    companion object {
        private val LEG_X0_Z0 = createCuboidShape(1.0, 0.0, 1.0, 4.0, 14.0, 4.0)
        private val LEG_X1_Z0 = createCuboidShape(12.0, 0.0, 1.0, 15.0, 14.0, 4.0)
        private val LEG_X0_Z1 = createCuboidShape(1.0, 0.0, 12.0, 4.0, 14.0, 15.0)
        private val LEG_X1_Z1 = createCuboidShape(12.0, 0.0, 12.0, 15.0, 14.0, 15.0)

        private val SHAPE = VoxelShapes.union(
            createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
            // Legs
            LEG_X0_Z0,
            LEG_X1_Z0,
            LEG_X0_Z1,
            LEG_X1_Z1
        )
    }
}
