package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import juuxel.adorn.api.util.BlockVariant
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

class PlatformBlock(variant: BlockVariant) : Block(variant.settings), PolyesterBlock, Fluidloggable {
    override val name = "${variant.id.path}_platform"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)
    override val hasDescription = true
    override val descriptionKey = "block.adorn.platform.desc"

    override fun getOutlineShape(p0: BlockState?, p1: BlockView?, p2: BlockPos?, context: EntityContext?): VoxelShape =
        COMBINED_SHAPE

    companion object {
        internal val POST_SHAPE = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
        private val PLATFORM_SHAPE = createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0)
        private val COMBINED_SHAPE = VoxelShapes.union(POST_SHAPE, PLATFORM_SHAPE)
    }
}
