package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import net.minecraft.stat.StatFormatter
import net.minecraft.stat.Stats
import net.minecraft.util.Identifier

object AdornStats {
    val OPEN_DRAWER: Identifier = register("open_drawer", StatFormatter.DEFAULT)
    val OPEN_KITCHEN_CUPBOARD: Identifier = register("open_kitchen_cupboard", StatFormatter.DEFAULT)
    val INTERACT_WITH_SHELF: Identifier = register("interact_with_shelf", StatFormatter.DEFAULT)
    val INTERACT_WITH_TABLE_LAMP: Identifier = register("interact_with_table_lamp", StatFormatter.DEFAULT)
    val INTERACT_WITH_TRADING_STATION: Identifier = register("interact_with_trading_station", StatFormatter.DEFAULT)
    val DYE_TABLE_LAMP: Identifier = register("dye_table_lamp", StatFormatter.DEFAULT)
    val DYE_SOFA: Identifier = register("dye_sofa", StatFormatter.DEFAULT)
    val SIT_ON_CHAIR: Identifier = register("sit_on_chair", StatFormatter.DEFAULT)
    val SIT_ON_SOFA: Identifier = register("sit_on_sofa", StatFormatter.DEFAULT)
    val SIT_ON_BENCH: Identifier = register("sit_on_bench", StatFormatter.DEFAULT)

    private fun register(id: String, formatter: StatFormatter) =
        Stats.register("${AdornCommon.NAMESPACE}:$id", formatter)

    fun init() {
    }
}
