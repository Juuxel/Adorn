package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CampfireBlock
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World
import virtuoel.towelette.api.Fluidloggable
import java.util.*

class ChimneyBlock : Block(FabricBlockSettings.copy(Blocks.BRICKS).ticksRandomly().build()), PolyesterBlock, Fluidloggable {
    override val name = "chimney"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)
    override val hasDescription = true

    @Environment(EnvType.CLIENT)
    override fun randomDisplayTick(state: BlockState?, world: World, pos: BlockPos, random: Random) {
        for (i in 0 until 2 + random.nextInt(2)) {
            world.addImportantParticle(
                ParticleTypes.CAMPFIRE_COSY_SMOKE, true,
                pos.x + 0.3 + random.nextDouble() * 0.4,
                pos.y + 0.9 + random.nextDouble() + random.nextDouble(),
                pos.z + 0.3 + random.nextDouble() * 0.4,
                0.0, 0.07, 0.0
            )
        }
    }

    override fun getTickRate(world: ViewableWorld?) = 15

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?) =
        SHAPE

    companion object {
        private val SHAPE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 12.0, 12.0)
    }
}
