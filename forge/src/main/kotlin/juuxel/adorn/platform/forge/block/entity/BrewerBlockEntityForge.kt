package juuxel.adorn.platform.forge.block.entity

import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.platform.forge.util.FluidTankReference
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.wrapper.SidedInvWrapper

class BrewerBlockEntityForge(pos: BlockPos, state: BlockState) : BrewerBlockEntity(pos, state) {
    private var itemHandlers = createItemHandlers()
    val tank = object : FluidTank(FLUID_CAPACITY_IN_BUCKETS * FluidType.BUCKET_VOLUME) {
        override fun onContentsChanged() {
            markDirty()
        }
    }
    override val fluidReference: FluidReference = FluidTankReference(tank)
    private val tankHolder = LazyOptional.of { tank }

    private fun createItemHandlers(): Array<LazyOptional<IItemHandlerModifiable>> =
        SidedInvWrapper.create(this, *Direction.values().sortedArrayWith(compareBy { it.id }))

    override fun canExtractFluidContainer() =
        !FluidUtil.tryEmptyContainer(getStack(FLUID_CONTAINER_SLOT), tank, tank.space, null, false).isSuccess

    override fun tryExtractFluidContainer() {
        val result = FluidUtil.tryEmptyContainer(getStack(FLUID_CONTAINER_SLOT), tank, tank.space, null, true)

        if (result.isSuccess) {
            setStack(FLUID_CONTAINER_SLOT, result.result)
            markDirty()
        }
    }

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (!removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return if (side != null) itemHandlers[side.id].cast() else LazyOptional.empty()
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return tankHolder.cast()
        }

        return super.getCapability(cap, side)
    }

    override fun invalidateCaps() {
        super.invalidateCaps()

        for (itemHandler in itemHandlers) {
            itemHandler.invalidate()
        }
    }

    override fun reviveCaps() {
        super.reviveCaps()
        itemHandlers = createItemHandlers()
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
