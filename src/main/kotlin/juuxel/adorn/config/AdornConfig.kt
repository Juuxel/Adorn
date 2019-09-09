package juuxel.adorn.config

import blue.endless.jankson.Comment

class AdornConfig {
    @Comment("If true, you can sit on tables.")
    @JvmField
    var sittingOnTables: Boolean = false

    @Comment("If true, sleeping on sofas can skip the night.")
    @JvmField
    // TODO: Should be a game rule
    var skipNightOnSofas: Boolean = true

    @Comment("Protects trading stations from other players.")
    @JvmField
    // TODO: Should be a game rule
    var protectTradingStations: Boolean = true

    @Comment("If true, enables debug features (currently: /adorn resource).")
    @JvmField
    var debug: Boolean = false

    @Comment("Configuration for Adorn's Extra Pieces support.")
    @JvmField
    var extraPieces: EPConfig = EPConfig()

    class EPConfig {
        @Comment("If true, enables carpeting for all Adorn chair and table piece blocks (warning: resource heavy).")
        @JvmField
        var carpetedEverything: Boolean = false

        @Comment("A list of piece sets that will have carpeting support.")
        @JvmField
        var carpetedPieceSets: List<String> = ArrayList() // ArrayList so that Jankson deserializes it properly
    }
}
