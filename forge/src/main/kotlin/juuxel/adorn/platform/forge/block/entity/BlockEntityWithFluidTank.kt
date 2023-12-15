package juuxel.adorn.platform.forge.block.entity

import net.neoforged.neoforge.fluids.capability.templates.FluidTank

interface BlockEntityWithFluidTank {
    val tank: FluidTank
}
