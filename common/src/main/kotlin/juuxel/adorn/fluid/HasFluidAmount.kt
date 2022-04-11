package juuxel.adorn.fluid

/**
 * A fluid volume-like object that has a fluid amount and unit.
 */
interface HasFluidAmount {
    val amount: Long
    val unit: FluidUnit
}
