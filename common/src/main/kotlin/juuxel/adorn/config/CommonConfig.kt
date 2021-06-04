package juuxel.adorn.config

interface CommonConfig {
    // TODO: This one HAS TO GO.
    val protectTradingStations: Boolean

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
