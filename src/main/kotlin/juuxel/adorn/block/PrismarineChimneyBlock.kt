package juuxel.adorn.block

import java.util.Random
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BubbleColumnBlock
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldView

open class PrismarineChimneyBlock(settings: Settings) : AbstractChimneyBlock(settings), BlockWithDescription {
    override val descriptionKey get() = AdornBlocks.PRISMARINE_CHIMNEY.translationKey + ".desc"

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

    override fun getTickRate(world: WorldView) = if (world.isClient) 3 else 20

    class WithColumn(val drag: Boolean, settings: Settings) : PrismarineChimneyBlock(settings) {
        override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
            BubbleColumnBlock.update(world, pos.up(), drag)
        }

        override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, moved: Boolean) {
            world.blockTickScheduler.schedule(pos, this, getTickRate(world))
        }

        override fun neighborUpdate(
            state: BlockState, world: World, pos: BlockPos, neighbor: Block, neighborPos: BlockPos, moved: Boolean
        ) {
            world.blockTickScheduler.schedule(pos, this, getTickRate(world))
        }

        @Environment(EnvType.CLIENT)
        override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
            if (!drag) {
                super.randomDisplayTick(state, world, pos, random)
            }
        }
    }
}
