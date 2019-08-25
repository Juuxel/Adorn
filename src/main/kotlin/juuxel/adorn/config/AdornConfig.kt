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
}
