package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityContext
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.particle.ParticleTypes
import net.minecraft.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World
import virtuoel.towelette.api.Fluidloggable
import java.util.*

class BubbleChimneyBlock : Block(Settings.copy(Blocks.PRISMARINE)), PolyesterBlock, Fluidloggable {
    override val name = "bubble_chimney"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)
    override val hasDescription = true

    @Environment(EnvType.CLIENT)
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (!getFluidState(state).matches(FluidTags.WATER)) return

        val x = pos.x + 0.5
        val y = pos.y + 0.9
        val z = pos.z + 0.5

        for (i in 1..3) {
            world.addImportantParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0.0, 0.0, 0.0)
        }
    }

    override fun hasRandomTicks(p0: BlockState?) = true
    override fun getTickRate(p0: ViewableWorld?) = 3
    override fun getOutlineShape(p0: BlockState?, p1: BlockView?, p2: BlockPos?, context: EntityContext?) = shape

    companion object {
        private val shape = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 12.0, 12.0)
    }
}
