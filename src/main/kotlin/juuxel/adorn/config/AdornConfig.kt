package juuxel.adorn.config

import me.sargunvohra.mcmods.autoconfig1.ConfigData
import me.sargunvohra.mcmods.autoconfig1.annotation.Config
import me.sargunvohra.mcmods.autoconfig1.annotation.ConfigEntry
import me.sargunvohra.mcmods.autoconfig1.shadowed.blue.endless.jankson.Comment

@Config(name = "Adorn")
@Config.Gui.Background("minecraft:textures/block/lime_wool.png")
class AdornConfig : ConfigData {
    @Comment("If true, you can sit on tables.")
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip
    @JvmField
    var sittingOnTables: Boolean = false

    @Comment("If true, sleeping on sofas can skip the night.")
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip
    @JvmField
    // TODO: Should be a game rule
    var skipNightOnSofas: Boolean = true

    @Comment("Protects trading stations from other players.")
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.Tooltip
    @JvmField
    // TODO: Should be a game rule
    var protectTradingStations: Boolean = true

    @Comment("Enable old stone rods for backwards compatibility.")
    @ConfigEntry.Category("advanced")
    @ConfigEntry.Gui.Tooltip
    @JvmField
    var enableOldStoneRods: Boolean = true
}
