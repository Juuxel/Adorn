package juuxel.adorn.criterion

import juuxel.adorn.platform.PlatformBridges
import net.minecraft.advancement.criterion.Criterion

object AdornCriteria {
    val SIT_ON_BLOCK: SitOnBlockCriterion = register(SitOnBlockCriterion())
    val BOUGHT_FROM_TRADING_STATION: BoughtFromTradingStationCriterion = register(BoughtFromTradingStationCriterion())

    fun init() {
    }

    private fun <T : Criterion<*>> register(criterion: T): T =
        PlatformBridges.criteria.register(criterion)
}
