@file:Suppress("OVERRIDE_DEPRECATION")

package juuxel.adorn.block

import net.minecraft.block.BlockState
import net.minecraft.block.Oxidizable
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random

class OxidizableCopperPipeBlock(
    private val oxidationLevel: Oxidizable.OxidationLevel,
    settings: Settings
) : CopperPipeBlock(settings), Oxidizable {
    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        tickDegradation(state, world, pos, random)
    }

    override fun hasRandomTicks(state: BlockState): Boolean =
        Oxidizable.getIncreasedOxidationBlock(state.block).isPresent

    override fun getDegradationLevel(): Oxidizable.OxidationLevel =
        oxidationLevel
}
