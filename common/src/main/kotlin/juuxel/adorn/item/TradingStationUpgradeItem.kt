package juuxel.adorn.item

import juuxel.adorn.util.enumMapOf
import net.minecraft.item.Item

class TradingStationUpgradeItem(settings: Settings, val type: Type) : ItemWithDescription(settings) {
    // Don't allow putting trading station storages inside shulker boxes or trading stations.
    override fun canBeNested(): Boolean = (type != Type.STORAGE)

    companion object {
        val BY_TYPE: Map<Type, Item> by lazy {
            enumMapOf(
                Type.IO to AdornItems.TRADING_STATION_IO_UPGRADE,
                Type.LINK to AdornItems.TRADING_STATION_LINK_UPGRADE,
                Type.VOID to AdornItems.TRADING_STATION_VOID_UPGRADE,
                Type.INFINITE_STOCK to AdornItems.TRADING_STATION_INFINITE_STOCK_UPGRADE,
            )
        }
    }

    /**
     * A kind of trading station upgrade.
     *
     * @property exclusive If true, only one copy of this upgrade may exist in the same trading station.
     */
    enum class Type(val exclusive: Boolean, val panelHeight: Int) {
        STORAGE(exclusive = false, panelHeight = 2 * 7 + 3 * 18 + 4),
        IO(exclusive = true, panelHeight = 50 + 4),
        LINK(exclusive = true, panelHeight = 0) {
            // Links cannot be combined with other upgrades.
            override fun canCombineWith(other: Type): Boolean = false
        },
        VOID(exclusive = true, panelHeight = 0),
        INFINITE_STOCK(exclusive = true, panelHeight = 0),
        ;

        protected open fun canCombineWith(other: Type): Boolean = true

        companion object {
            fun canCombine(a: Type, b: Type): Boolean {
                if (a == b) {
                    return !a.exclusive
                }

                return a.canCombineWith(b) && b.canCombineWith(a)
            }
        }
    }
}
