package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import juuxel.adorn.util.BlockVariant
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import virtuoel.towelette.api.Fluidloggable

class StepBlock(variant: BlockVariant) : Block(variant.settings), PolyesterBlock, Fluidloggable {
    override val name = "${variant.id.path}_step"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)
    override val hasDescription = true
    override val descriptionKey = "block.adorn.step.desc"

    override fun getOutlineShape(p0: BlockState?, p1: BlockView?, p2: BlockPos?, context: EntityContext?): VoxelShape =
        SHAPE

    companion object {
        private val SHAPE = VoxelShapes.union(
            /* Post     */ createCuboidShape(6.0, 0.0, 6.0, 10.0, 8.0, 10.0),
            /* Platform */ createCuboidShape(0.0, 6.0, 0.0, 16.0, 8.0, 16.0)
        )
    }
}
