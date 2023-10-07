package juuxel.adorn.criterion

import juuxel.adorn.AdornCommon
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.advancement.criterion.Criterion
import net.minecraft.util.Identifier

object AdornCriteria {
    val SIT_ON_BLOCK: SitOnBlockCriterion = register("sit_on_block", SitOnBlockCriterion())
    val BOUGHT_FROM_TRADING_STATION: BoughtFromTradingStationCriterion = register("bought_from_trading_station", BoughtFromTradingStationCriterion())

    fun init() {
    }

    private fun <C : Criterion<*>> register(id: String, criterion: C): C =
        Criteria.register("${AdornCommon.NAMESPACE}${Identifier.NAMESPACE_SEPARATOR}$id", criterion)
}
