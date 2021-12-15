package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.KitchenSinkEjection
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.fluid.Fluids
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object KitchenSinkEjectionImpl : KitchenSinkEjection {
    private val water = FluidVariant.of(Fluids.WATER)

    override fun eject(world: World, pos: BlockPos) {
        for (direction in Direction.values()) {
            val otherPos = pos.offset(direction)

            Transaction.openOuter().use {
                FluidStorage.SIDED.find(world, otherPos, world.getBlockState(otherPos), world.getBlockEntity(otherPos), direction.opposite)
                    ?.insert(water, Long.MAX_VALUE, it)
                it.commit()
            }
        }
    }
}
