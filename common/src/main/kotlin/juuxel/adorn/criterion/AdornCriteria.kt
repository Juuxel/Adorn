package juuxel.adorn.criterion

import net.minecraft.advancement.criterion.Criteria.register

object AdornCriteria {
    val SIT_ON_BLOCK: SitOnBlockCriterion = register(SitOnBlockCriterion())
    val BOUGHT_FROM_TRADING_STATION: BoughtFromTradingStationCriterion = register(BoughtFromTradingStationCriterion())

    fun init() {
    }
}
