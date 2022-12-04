package juuxel.adorn.compat.emi

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import juuxel.adorn.fluid.FluidIngredient
import juuxel.adorn.fluid.FluidUnit
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant

fun FluidIngredient.toEmiIngredient(): EmiIngredient {
    val amount = FluidUnit.convert(amount, unit, FluidUnit.DROPLET)
    // Has to be a set since flowing/still normalisation might reduce the number
    // of unique fluids
    val variants = fluid.getFluids().mapTo(HashSet()) {
        FluidVariant.of(it, nbt)
    }
    return EmiIngredient.of(variants.map { EmiStack.of(it, amount) })
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
