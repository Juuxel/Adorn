package juuxel.adorn.lib;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.resources.Identifier;

public final class AdornStats {
    public static final Registrar<Identifier> CUSTOM_STATS = RegistrarFactory.get().create(Registries.CUSTOM_STAT);
    public static final Identifier OPEN_BREWER = register("open_brewer");
    public static final Identifier OPEN_DRAWER = register("open_drawer");
    public static final Identifier OPEN_KITCHEN_CUPBOARD = register("open_kitchen_cupboard");
    public static final Identifier INTERACT_WITH_SHELF = register("interact_with_shelf");
    public static final Identifier INTERACT_WITH_TABLE_LAMP = register("interact_with_table_lamp");
    public static final Identifier INTERACT_WITH_TRADING_STATION = register("interact_with_trading_station");
    public static final Identifier DYE_TABLE_LAMP = register("dye_table_lamp");
    public static final Identifier DYE_SOFA = register("dye_sofa");
    public static final Identifier SIT_ON_CHAIR = register("sit_on_chair");
    public static final Identifier SIT_ON_SOFA = register("sit_on_sofa");
    public static final Identifier SIT_ON_BENCH = register("sit_on_bench");

    private static Identifier register(String name) {
        var id = AdornCommon.id(name);
        CUSTOM_STATS.register(name, () -> id);
        return id;
    }

    public static void init() {
        for (Identifier stat : CUSTOM_STATS) {
            Stats.CUSTOM.get(stat, StatFormatter.DEFAULT);
        }
    }
}
