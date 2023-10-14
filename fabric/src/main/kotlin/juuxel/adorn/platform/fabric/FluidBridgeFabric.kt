package juuxel.adorn.platform.fabric

import juuxel.adorn.fluid.FluidAmountPredicate
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.FluidVolume
import juuxel.adorn.platform.FluidBridge
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluid
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class FluidBridgeFabric : FluidBridge {
    override val fluidUnit = FluidUnit.DROPLET

    override fun drain(world: World, pos: BlockPos, state: BlockState?, side: Direction, fluid: Fluid, amountPredicate: FluidAmountPredicate): FluidVolume? {
        val storage = FluidStorage.SIDED.find(world, pos, state, null, side)

        if (storage != null) {
            val upperBound = amountPredicate.upperBound
            val maxAmount = FluidUnit.convert(upperBound.amount, from = upperBound.unit, to = FluidUnit.DROPLET)
            Transaction.openOuter().use { transaction ->
                val extracted = storage.extract(FluidVariant.of(fluid), maxAmount, transaction)

                if (extracted > 0 && amountPredicate.test(extracted, FluidUnit.DROPLET)) {
                    transaction.commit()
                    return FluidVolume(fluid, extracted, null, FluidUnit.DROPLET)
                }
            }
        }

        return null
    }
}
