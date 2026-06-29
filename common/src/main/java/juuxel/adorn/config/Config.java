package juuxel.adorn.config;

import blue.endless.jankson.Comment;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.item.group.ItemGroupingOption;

import java.util.HashMap;
import java.util.Map;

public final class Config {
    @Comment("How items will be grouped in Adorn's creative tab")
    public ItemGroupingOption groupItems = ItemGroupingOption.BY_MATERIAL;

    @Comment("Client-side settings")
    public Client client = new Client();

    @Comment("Default values for game rules in new worlds")
    public GameRuleDefaults gameRuleDefaults = new GameRuleDefaults();

    @Comment("Mod compatibility toggles (enabled: true, disabled: false)")
    public Map<String, Boolean> compat = new HashMap<>();

    public static final class Client {
        @Comment("If true, floating tooltips are shown above trading stations.")
        public boolean showTradingStationTooltips = true;

        @Comment("If true, Adorn items will also be shown in matching vanilla item tabs.")
        public boolean showItemsInStandardGroups = true;

        @Comment("The fluid unit to show fluid amounts in. Options: [litres, droplets]")
        public FluidUnit displayedFluidUnit = FluidUnit.LITRE;
    }

    public static final class GameRuleDefaults {
        @Comment("If true, sleeping on sofas can skip the night.")
        public boolean skipNightOnSofas = true;

        @Comment("If true, kitchen sinks are infinite sources for infinite fluids.")
        public boolean infiniteKitchenSinks = true;

        @Comment("If true, broken trading stations drop a locked version with their contents inside.")
        public boolean dropLockedTradingStations = true;
    }
}
