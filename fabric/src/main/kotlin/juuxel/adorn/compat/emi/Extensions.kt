package juuxel.adorn.compat.emi

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import juuxel.adorn.fluid.FluidIngredient
import juuxel.adorn.fluid.FluidUnit
import net.minecraft.item.Item

fun FluidIngredient.toEmiIngredient(): EmiIngredient {
    val amount = FluidUnit.convert(amount, unit, FluidUnit.DROPLET)
    return EmiIngredient.of(fluid.getFluids().map { EmiStack.of(it, nbt, amount) })
}

fun EmiIngredient.withRemainders(): EmiIngredient {
    for (stack in emiStacks) {
        stack.getKeyOfType(Item::class.java)?.let { item ->
            if (item.hasRecipeRemainder()) {
                stack.remainder = EmiStack.of(item.recipeRemainder)
            }
        }
    }

    return this
}
