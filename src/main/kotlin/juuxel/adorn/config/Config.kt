package juuxel.adorn.config

import blue.endless.jankson.Comment

class Config {
    @Comment("If true, you can sit on tables.")
    @JvmField
    var sittingOnTables: Boolean = false

    @Comment("Protects trading stations from other players.")
    @JvmField
    // TODO: Should be a game rule
    var protectTradingStations: Boolean = true

    @field:Comment("Client-side settings")
    var client: Client = Client()

    @field:Comment("Default values for game rules in new worlds")
    var gameRuleDefaults: GameRuleDefaults = GameRuleDefaults()

    @field:Comment("Mod compatibility toggles (enabled: true, disabled: false)")
    var compat: MutableMap<String, Boolean> = HashMap()

    @Comment("Configuration for Adorn's Extra Pieces support.")
    @JvmField
    var extraPieces: EPConfig = EPConfig()

    class Client {
        @field:Comment("If true, floating tooltips are shown above trading stations.")
        var showTradingStationTooltips: Boolean = true

        @field:Comment("If true, Adorn items will also be shown in matching vanilla item tabs.")
        var showItemsInStandardGroups: Boolean = true
    }

    class EPConfig {
        @Comment("If true, Adorn's EP support is enabled. You can disable it by setting this property to false.")
        @JvmField
        var enabled: Boolean = true

        @Comment("If true, Adorn's Towelette support will be enabled for EP blocks. (warning: resource heavy)")
        @JvmField
        var toweletteSupport: Boolean = false

        @Comment("If true, enables carpeting for all Adorn chair and table piece blocks (warning: resource heavy).")
        @JvmField
        var carpetedEverything: Boolean = false

        @Comment("A list of piece sets that will have carpeting support.")
        @JvmField
        var carpetedPieceSets: List<String> = ArrayList() // ArrayList so that Jankson deserializes it properly
    }

    class GameRuleDefaults {
        @field:Comment("If true, sleeping on sofas can skip the night.")
        var skipNightOnSofas: Boolean = true
    }
}
