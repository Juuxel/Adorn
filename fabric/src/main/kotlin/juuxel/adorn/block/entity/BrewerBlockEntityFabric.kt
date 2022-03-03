package juuxel.adorn.block.entity

import com.google.common.base.Predicates
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidUnit
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.BlockState
import net.minecraft.fluid.Fluid
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class BrewerBlockEntityFabric(pos: BlockPos, state: BlockState) : BrewerBlockEntity(pos, state) {
    val fluidStorage = object : SingleVariantStorage<FluidVariant>() {
        override fun getCapacity(variant: FluidVariant): Long =
            FLUID_CAPACITY_IN_BUCKETS * FluidConstants.BUCKET

        override fun getBlankVariant(): FluidVariant =
            FluidVariant.blank()

        override fun onFinalCommit() {
            markDirty()
        }
    }

    override val fluidReference = object : FluidReference() {
        override var fluid: Fluid
            get() = fluidStorage.variant.fluid
            set(value) {
                fluidStorage.variant = FluidVariant.of(value, fluidStorage.variant.nbt)
            }

        override var amount: Long
            get() = fluidStorage.amount
            set(value) {
                fluidStorage.amount = value
            }

        override var nbt: NbtCompound?
            get() = fluidStorage.variant.nbt
            set(value) {
                fluidStorage.variant = FluidVariant.of(fluidStorage.variant.fluid, value)
            }

        override val unit = FluidUnit.DROPLET
    }

    override fun canExtractFluidContainer() =
        Transaction.openOuter().use { extractFluidContainer(it) == 0L }

    override fun tryExtractFluidContainer() {
        extractFluidContainer(null)
    }

    private fun extractFluidContainer(transaction: TransactionContext?): Long {
        val fluidContainerSlot = InventoryStorage.of(this, null).getSlot(FLUID_CONTAINER_SLOT)
        val itemStorage = FluidStorage.ITEM.find(getStack(FLUID_CONTAINER_SLOT), ContainerItemContext.ofSingleSlot(fluidContainerSlot))
        return StorageUtil.move(itemStorage, fluidStorage, Predicates.alwaysTrue(), Long.MAX_VALUE, transaction)
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        nbt.put(NBT_FLUID, fluidStorage.variant.toNbt())
        nbt.putLong(NBT_VOLUME, fluidStorage.amount)
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        fluidStorage.variant = FluidVariant.fromNbt(nbt.getCompound(NBT_FLUID))
        fluidStorage.amount = nbt.getLong(NBT_VOLUME)
    }

    companion object {
        private const val NBT_FLUID = "Fluid"
        private const val NBT_VOLUME = "Volume"
    }
}
