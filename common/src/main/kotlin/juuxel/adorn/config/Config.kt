package juuxel.adorn.config

import blue.endless.jankson.Comment

class Config {
    @field:Comment("Client-side settings")
    var client: Client = Client()

    @field:Comment("Default values for game rules in new worlds")
    var gameRuleDefaults: GameRuleDefaults = GameRuleDefaults()

    @field:Comment("Mod compatibility toggles (enabled: true, disabled: false)")
    var compat: MutableMap<String, Boolean> = HashMap()

    class Client {
        @field:Comment("If true, floating tooltips are shown above trading stations.")
        var showTradingStationTooltips: Boolean = true

        @field:Comment("If true, Adorn items will also be shown in matching vanilla item tabs.")
        var showItemsInStandardGroups: Boolean = true
    }

    class GameRuleDefaults {
        @field:Comment("If true, sleeping on sofas can skip the night.")
        var skipNightOnSofas: Boolean = true
    }
}
