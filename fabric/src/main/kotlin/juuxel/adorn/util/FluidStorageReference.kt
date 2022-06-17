package juuxel.adorn.util

import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidUnit
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.minecraft.fluid.Fluid
import net.minecraft.nbt.NbtCompound

/**
 * A [fluid reference][FluidReference] to a [`SingleVariantStorage<FluidVariant>`][SingleVariantStorage].
 */
class FluidStorageReference(private val storage: SingleVariantStorage<FluidVariant>) : FluidReference() {
    val variant: FluidVariant get() = storage.variant

    override var fluid: Fluid
        get() = storage.variant.fluid
        set(value) {
            storage.variant = FluidVariant.of(value, storage.variant.nbt)
        }

    override var amount: Long
        get() = storage.amount
        set(value) {
            storage.amount = value
        }

    override var nbt: NbtCompound?
        get() = storage.variant.nbt
        set(value) {
            storage.variant = FluidVariant.of(storage.variant.fluid, value)
        }

    override val unit = FluidUnit.DROPLET
}
