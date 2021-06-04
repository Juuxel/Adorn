package juuxel.adorn.config

interface CommonConfig {
    val client: ClientConfig
    val gameRuleDefaults: GameRuleDefaultsConfig
    val compat: MutableMap<String, Boolean>

    interface ClientConfig {
        val showTradingStationTooltips: Boolean
        val showItemsInStandardGroups: Boolean
    }

    interface GameRuleDefaultsConfig {
        val skipNightOnSofas: Boolean
    }
}
