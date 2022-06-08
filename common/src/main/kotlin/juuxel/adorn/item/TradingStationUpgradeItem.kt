package juuxel.adorn.item

class TradingStationUpgradeItem(settings: Settings, val type: Type) : ItemWithDescription(settings) {
    // Don't allow putting trading station storages inside shulker boxes or trading stations.
    override fun canBeNested(): Boolean = (type != Type.STORAGE)

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
