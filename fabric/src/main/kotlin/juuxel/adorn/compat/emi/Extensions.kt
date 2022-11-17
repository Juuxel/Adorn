package juuxel.adorn.compat.emi

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import juuxel.adorn.fluid.FluidIngredient
import juuxel.adorn.fluid.FluidUnit
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant

fun FluidIngredient.toEmiIngredient(): EmiIngredient {
    val amount = FluidUnit.convert(amount, unit, FluidUnit.DROPLET)
    return EmiIngredient.of(
        fluid.getFluids().map {
            EmiStack.of(FluidVariant.of(it, nbt), amount)
        }
    )
}

fun EmiIngredient.withRemainders(): EmiIngredient {
    for (stack in emiStacks) {
        val entry = stack.entry
        if (ItemVariant::class.java.isAssignableFrom(entry.type)) {
            val item = (entry.value as ItemVariant).item
            if (item.hasRecipeRemainder()) {
                stack.remainder = EmiStack.of(item.recipeRemainder)
            }
        }
    }

    return this
}
