package juuxel.adorn.platform.forge

import juuxel.adorn.fluid.FluidAmountPredicate
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.FluidVolume
import juuxel.adorn.platform.FluidBridge
import juuxel.adorn.platform.forge.util.toFluidVolume
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluid
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class FluidBridgeForge : FluidBridge {
    override val fluidUnit = FluidUnit.LITRE

    override fun drain(world: World, pos: BlockPos, state: BlockState?, side: Direction, fluid: Fluid, amountPredicate: FluidAmountPredicate): FluidVolume? {
        // This method is a port of the Fabric fluid bridge code.
        val fluidHandler = world.getCapability(Capabilities.FluidHandler.BLOCK, pos, state, null, side)

        if (fluidHandler != null) {
            val upperBound = amountPredicate.upperBound
            val maxAmount = FluidUnit.convert(upperBound.amount, from = upperBound.unit, to = FluidUnit.LITRE).toInt()
            val max = FluidStack(fluid, maxAmount)
            val extracted = fluidHandler.drain(max, IFluidHandler.FluidAction.SIMULATE)

            if (!extracted.isEmpty && amountPredicate.test(extracted.amount.toLong(), FluidUnit.LITRE)) {
                fluidHandler.drain(extracted, IFluidHandler.FluidAction.EXECUTE)
                return extracted.toFluidVolume()
            }
        }

        return null
    }
}
