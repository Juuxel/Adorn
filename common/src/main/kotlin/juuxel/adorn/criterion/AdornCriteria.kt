package juuxel.adorn.criterion

import juuxel.adorn.lib.registry.Registrar
import juuxel.adorn.lib.registry.RegistrarFactory
import net.minecraft.advancement.criterion.Criterion
import net.minecraft.registry.RegistryKeys

object AdornCriteria {
    val CRITERIA: Registrar<Criterion<*>> = RegistrarFactory.get().create(RegistryKeys.CRITERION)
    val SIT_ON_BLOCK: SitOnBlockCriterion by CRITERIA.register("sit_on_block") { SitOnBlockCriterion() }
    val BOUGHT_FROM_TRADING_STATION: BoughtFromTradingStationCriterion by CRITERIA.register("bought_from_trading_station") {
        BoughtFromTradingStationCriterion()
    }

    fun init() {
    }
}
