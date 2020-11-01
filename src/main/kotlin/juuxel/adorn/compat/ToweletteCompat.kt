package juuxel.adorn.compat

import juuxel.adorn.block.ChairBlock
import juuxel.adorn.block.ChimneyBlock
import juuxel.adorn.block.CoffeeTableBlock
import juuxel.adorn.block.PicketFenceBlock
import juuxel.adorn.block.PlatformBlock
import juuxel.adorn.block.PostBlock
import juuxel.adorn.block.PrismarineChimneyBlock
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.SofaBlock
import juuxel.adorn.block.StepBlock
import juuxel.adorn.block.TableBlock
import juuxel.adorn.block.TableLampBlock
import juuxel.adorn.block.TradingStationBlock
import juuxel.adorn.compat.extrapieces.AdornPieceMarker
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.util.visit
import net.minecraft.block.Block
import net.minecraft.util.registry.Registry
import virtuoel.statement.api.StateRefresher
import virtuoel.towelette.api.FluidProperties
import virtuoel.towelette.api.ToweletteConfig

object ToweletteCompat {
    fun init() {
        val flowing = run {
            var result = false
            val data = ToweletteConfig.DATA["flowingFluidlogging"]

            if (data != null && data.isJsonPrimitive) {
                result = data.asBoolean
            }

            result
        }

        val fluidlogEp = ConfigManager.CONFIG.extraPieces.toweletteSupport
        Registry.BLOCK.visit {
            if (shouldFluidlog(it) && (it !is AdornPieceMarker || fluidlogEp)) {
                StateRefresher.INSTANCE.addBlockProperty(it, FluidProperties.FLUID, Registry.FLUID.defaultId)

                if (flowing) {
                    StateRefresher.INSTANCE.addBlockProperty(it, FluidProperties.LEVEL_1_8, 8)
                    StateRefresher.INSTANCE.addBlockProperty(it, FluidProperties.FALLING, false)
                }
            }
        }
    }

    private fun shouldFluidlog(block: Block): Boolean =
        block is ChairBlock ||
            block is ChimneyBlock ||
            block is CoffeeTableBlock ||
            block is PicketFenceBlock ||
            block is PlatformBlock ||
            block is PostBlock ||
            block is PrismarineChimneyBlock ||
            block is ShelfBlock ||
            block is SofaBlock ||
            block is StepBlock ||
            block is TableBlock ||
            block is TableLampBlock ||
            block is TradingStationBlock
}
