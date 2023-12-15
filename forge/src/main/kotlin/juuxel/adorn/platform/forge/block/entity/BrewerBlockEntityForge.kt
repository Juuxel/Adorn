package juuxel.adorn.platform.forge.block.entity

import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.platform.forge.util.FluidTankReference
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

class BrewerBlockEntityForge(pos: BlockPos, state: BlockState) : BrewerBlockEntity(pos, state), BlockEntityWithFluidTank {
    override val tank = object : FluidTank(FLUID_CAPACITY_IN_BUCKETS * FluidType.BUCKET_VOLUME) {
        override fun onContentsChanged() {
            markDirty()
        }
    }
    override val fluidReference: FluidReference = FluidTankReference(tank)

    override fun canExtractFluidContainer() =
        !FluidUtil.tryEmptyContainer(getStack(FLUID_CONTAINER_SLOT), tank, tank.space, null, false).isSuccess

    override fun tryExtractFluidContainer() {
        val result = FluidUtil.tryEmptyContainer(getStack(FLUID_CONTAINER_SLOT), tank, tank.space, null, true)

        if (result.isSuccess) {
            setStack(FLUID_CONTAINER_SLOT, result.result)
            markDirty()
        }
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        tank.writeToNBT(nbt)
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        tank.readFromNBT(nbt)
    }
}
