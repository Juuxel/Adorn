package juuxel.adorn.block.builder

import io.github.juuxel.polyester.block.WoodType
import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.block.*
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem

/**
 * @since 0.2.0
 */
class WoodFurnitureBuilder private constructor(private val woodType: WoodType) {
    private var drawer = false
    private var chair = false
    private var table = false
    private var kitchenCounter = false
    private var kitchenCupboard = false

    fun withDrawer() = apply {
        drawer = true
    }

    fun withChair() = apply {
        chair = true
    }

    fun withTable() = apply {
        table = true
    }

    fun withKitchenCounter() = apply {
        kitchenCounter = true
    }

    fun withKitchenCupboard() = apply {
        kitchenCupboard = true
    }

    fun register(): Unit = Registry().register()

    private inner class Registry : PolyesterRegistry(woodType.id.namespace) {
        private val material = woodType.id.path

        fun register() {
            if (drawer) registerBlock(DrawerBlock(material))
            if (chair) {
                val block = registerBlock(ChairBlock(material))
                registerItem(ChairBlockItem(block))
            }
            if (table) {
                val block = registerBlock(TableBlock(material))
                registerItem(TableBlockItem(block))
            }
            if (kitchenCounter) registerBlock(KitchenCounterBlock(material))
            if (kitchenCupboard) registerBlock(KitchenCupboardBlock(material))
        }
    }

    companion object {
        @JvmStatic
        fun create(woodType: WoodType): WoodFurnitureBuilder =
            WoodFurnitureBuilder(woodType)
    }
}
