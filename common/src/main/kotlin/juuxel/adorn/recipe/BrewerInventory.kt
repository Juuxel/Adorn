package juuxel.adorn.recipe

import juuxel.adorn.fluid.FluidReference
import net.minecraft.inventory.Inventory

interface BrewerInventory : Inventory {
    val fluidReference: FluidReference
}
