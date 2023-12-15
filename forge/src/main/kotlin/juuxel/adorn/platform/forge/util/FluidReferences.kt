package juuxel.adorn.platform.forge.util

import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.FluidVolume
import net.minecraft.fluid.Fluid
import net.minecraft.nbt.NbtCompound
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

/**
 * A [fluid reference][FluidReference] to a [FluidTank].
 */
class FluidTankReference(val tank: FluidTank) : FluidReference() {
    override var fluid: Fluid
        get() = tank.fluid.fluid
        set(value) {
            tank.fluid = FluidStack(value, tank.fluid.amount, tank.fluid.tag)
        }

    override var amount: Long
        get() = tank.fluid.amount.toLong()
        set(value) {
            tank.fluid.amount = value.toInt()
        }

    override var nbt: NbtCompound?
        get() = tank.fluid.tag
        set(value) {
            tank.fluid.tag = value
        }

    override val unit = FluidUnit.LITRE
}

/**
 * Converts this fluid reference to a [FluidStack].
 * This is faster than a manual conversion for a [FluidTankReference].
 */
fun FluidReference.toFluidStack(): FluidStack =
    if (this is FluidTankReference) {
        tank.fluid
    } else {
        FluidStack(fluid, amount.toInt(), nbt)
    }

fun FluidStack.toFluidVolume(): FluidVolume =
    FluidVolume(fluid, amount.toLong(), tag, FluidUnit.LITRE)
