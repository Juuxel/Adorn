package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World
import virtuoel.towelette.api.Fluidloggable
import java.util.*

class ChimneyBlock : Block(Settings.copy(Blocks.BRICKS)), PolyesterBlock, Fluidloggable {
    override val name = "chimney"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)
    override val hasDescription = true

    @Environment(EnvType.CLIENT)
    override fun randomDisplayTick(state: BlockState?, world: World, pos: BlockPos, random: Random) {
        val x = pos.x + 0.5
        val y = pos.y + 0.9
        val z = pos.z + 0.5

        for (i in 1..3) {
            world.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.0, 0.0, 0.0)
        }
    }

    override fun hasRandomTicks(state: BlockState?) = true
    override fun getTickRate(world: ViewableWorld?) = 3
    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?) =
        SHAPE

    companion object {
        private val SHAPE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 12.0, 12.0)
    }
}
