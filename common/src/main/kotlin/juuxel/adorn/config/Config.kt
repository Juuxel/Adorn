package juuxel.adorn.config

import blue.endless.jankson.Comment
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.item.group.ItemGroupingOption

class Config {
    @JvmField
    @field:Comment("How items will be grouped in Adorn's creative tab")
    var groupItems: ItemGroupingOption = ItemGroupingOption.BY_MATERIAL

    @JvmField
    @field:Comment("Client-side settings")
    var client: Client = Client()

    @JvmField
    @field:Comment("Default values for game rules in new worlds")
    var gameRuleDefaults: GameRuleDefaults = GameRuleDefaults()

    @JvmField
    @field:Comment("Mod compatibility toggles (enabled: true, disabled: false)")
    var compat: MutableMap<String, Boolean> = HashMap()

    class Client {
        @JvmField
        @field:Comment("If true, floating tooltips are shown above trading stations.")
        var showTradingStationTooltips: Boolean = true

        @JvmField
        @field:Comment("If true, Adorn items will also be shown in matching vanilla item tabs.")
        var showItemsInStandardGroups: Boolean = true

        @JvmField
        @field:Comment("The fluid unit to show fluid amounts in. Options: [litres, droplets]")
        var displayedFluidUnit: FluidUnit = FluidUnit.LITRE
    }

    class GameRuleDefaults {
        @JvmField
        @field:Comment("If true, sleeping on sofas can skip the night.")
        var skipNightOnSofas: Boolean = true

        @JvmField
        @field:Comment("If true, kitchen sinks are infinite sources for infinite fluids.")
        var infiniteKitchenSinks: Boolean = true

        @JvmField
        @field:Comment("If true, broken trading stations drop a locked version with their contents inside.")
        var dropLockedTradingStations: Boolean = true
    }
}
