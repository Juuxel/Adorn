package juuxel.adorn.platform

import juuxel.adorn.fluid.FluidAmountPredicate
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.FluidVolume
import juuxel.adorn.util.InlineServices
import juuxel.adorn.util.loadService
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluid
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

interface FluidBridge {
    val fluidUnit: FluidUnit

    fun drain(world: World, pos: BlockPos, state: BlockState?, side: Direction, fluid: Fluid, amountPredicate: FluidAmountPredicate): FluidVolume?

    @InlineServices
    companion object {
        private val instance: FluidBridge by lazy { loadService() }

        @JvmStatic
        fun get(): FluidBridge = instance
    }
}
