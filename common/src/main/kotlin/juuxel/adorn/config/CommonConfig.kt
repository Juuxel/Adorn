package juuxel.adorn.config

interface CommonConfig {
    val client: ClientConfig
    val gameRuleDefaults: GameRuleDefaultsConfig

    interface ClientConfig {
        val showTradingStationTooltips: Boolean
        val showItemsInStandardGroups: Boolean

        object Default : ClientConfig {
            override val showTradingStationTooltips = true
            override val showItemsInStandardGroups = true
        }
    }

    interface GameRuleDefaultsConfig {
        val skipNightOnSofas: Boolean

        object Default : GameRuleDefaultsConfig {
            override val skipNightOnSofas = true
        }
    }

    object Default : CommonConfig {
        override val client = ClientConfig.Default
        override val gameRuleDefaults = GameRuleDefaultsConfig.Default
    }
}
