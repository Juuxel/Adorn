package juuxel.adorn.fluid

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.item.AdornItems
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.fluid.FlowableFluid

object AdornFluids {
    val FLUIDS = PlatformBridges.registrarFactory.fluid()
    val FLOWING_SWEET_BERRY_JUICE: DrinkFluid by FLUIDS.register("flowing_sweet_berry_juice", createSweetBerryJuice(isStill = false))
    val SWEET_BERRY_JUICE: DrinkFluid by FLUIDS.register("sweet_berry_juice", createSweetBerryJuice(isStill = true))

    fun init() {
    }

    private fun createSweetBerryJuice(isStill: Boolean): () -> DrinkFluid =
        { DrinkFluid(
            isStill,
            flowing = { FLOWING_SWEET_BERRY_JUICE },
            still = { SWEET_BERRY_JUICE },
            block = { AdornBlocks.SWEET_BERRY_JUICE },
            bucket = { AdornItems.SWEET_BERRY_JUICE_BUCKET },
            color = 0xAF2628
        ) }
}
