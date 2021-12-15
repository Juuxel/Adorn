package juuxel.adorn.transfer

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.fluid.Fluids

object InfiniteWaterStorage: Storage<FluidVariant> {

    private val water = FluidVariant.of(Fluids.WATER)
    private val list = listOf(InfiniteWaterStorageView)

    override fun insert(resource: FluidVariant?, maxAmount: Long, transaction: TransactionContext?): Long {
        // We are a perfect sink
        return if (resource == water) {
            maxAmount
        } else {
            0
        }
    }

    override fun extract(resource: FluidVariant?, maxAmount: Long, transaction: TransactionContext?): Long {
        // We are a perfect source
        return if (resource == water) {
            maxAmount
        } else {
            0
        }
    }

    override fun iterator(transaction: TransactionContext?): Iterator<StorageView<FluidVariant>> {
        return list.iterator()
    }

    private object InfiniteWaterStorageView : StorageView<FluidVariant> {
        override fun extract(resource: FluidVariant?, maxAmount: Long, transaction: TransactionContext?): Long {
            return InfiniteWaterStorage.extract(resource, maxAmount, transaction)
        }

        override fun isResourceBlank(): Boolean {
            return water.isBlank
        }

        override fun getResource(): FluidVariant {
            return water
        }

        override fun getAmount(): Long {
            return Long.MAX_VALUE
        }

        override fun getCapacity(): Long {
            return Long.MAX_VALUE
        }
    }
}
