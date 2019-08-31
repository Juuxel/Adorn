package juuxel.adorn.block

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.particle.ParticleTypes
import net.minecraft.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World
import java.util.*

class PrismarineChimneyBlock : AbstractChimneyBlock(
    FabricBlockSettings.copy(Blocks.PRISMARINE).ticksRandomly().build()
), BlockWithDescription {
    @Environment(EnvType.CLIENT)
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (!state.fluidState.matches(FluidTags.WATER) || state[CONNECTED]) return

        val x = pos.x + 0.5
        val y = pos.y + 0.9
        val z = pos.z + 0.5

        for (i in 1..3) {
            world.addImportantParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0.0, 0.0, 0.0)
        }
    }

    override fun getTickRate(p0: ViewableWorld?) = 3
}
