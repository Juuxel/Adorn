package juuxel.adorn.api.builder

import juuxel.adorn.api.util.BlockVariant
import juuxel.adorn.block.*
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.polyester.block.WoodType
import juuxel.polyester.registry.PolyesterRegistry
import org.apiguardian.api.API

/**
 * @since 1.1.0
 */
@API(status = API.Status.EXPERIMENTAL, since = "1.1.0")
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

    fun register(namespace: String): Unit = Registry(namespace).register()

    private inner class Registry(namespace: String) : PolyesterRegistry(namespace) {
        private val material = woodType.id.path
        private val variant = BlockVariant.Wood(material)

        fun register() {
            if (drawer) registerBlock(DrawerBlock(variant))
            if (chair) {
                val block = registerBlock(ChairBlock(variant))
                registerItem(ChairBlockItem(block))
            }
            if (table) {
                val block = registerBlock(TableBlock(variant))
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
