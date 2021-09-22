package juuxel.adorn.config

import blue.endless.jankson.Comment

class Config : CommonConfig {
    @field:Comment("Client-side settings")
    override var client: Client = Client()

    @field:Comment("Default values for game rules in new worlds")
    override var gameRuleDefaults: GameRuleDefaults = GameRuleDefaults()

    @field:Comment("Mod compatibility toggles (enabled: true, disabled: false)")
    var compat: MutableMap<String, Boolean> = HashMap()

    class Client : CommonConfig.ClientConfig {
        @field:Comment("If true, floating tooltips are shown above trading stations.")
        override var showTradingStationTooltips: Boolean = CommonConfig.ClientConfig.Default.showTradingStationTooltips

        @field:Comment("If true, Adorn items will also be shown in matching vanilla item tabs.")
        override var showItemsInStandardGroups: Boolean = CommonConfig.ClientConfig.Default.showItemsInStandardGroups
    }

    class GameRuleDefaults : CommonConfig.GameRuleDefaultsConfig {
        @field:Comment("If true, sleeping on sofas can skip the night.")
        override var skipNightOnSofas: Boolean = CommonConfig.GameRuleDefaultsConfig.Default.skipNightOnSofas
    }
}
