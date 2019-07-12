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

    @Comment("Enable old stone rods for backwards compatibility.")
    @ConfigEntry.Category("advanced")
    @ConfigEntry.Gui.Tooltip
    @JvmField
    var enableOldStoneRods: Boolean = true
}
