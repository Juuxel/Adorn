package juuxel.adorn.api.block

import juuxel.adorn.api.util.BlockVariant
import juuxel.adorn.block.*
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.polyester.registry.PolyesterRegistry

class AdornBlockBuilder private constructor(private val material: BlockVariant) {
    private var post = false
    private var platform = false
    private var step = false
    private var drawer = false
    private var chair = false
    private var table = false
    private var kitchenCounter = false
    private var kitchenCupboard = false
    private var shelf = false

    fun withPost() = apply {
        post = true
    }

    fun withPlatform() = apply {
        platform = true
    }

    fun withStep() = apply {
        step = true
    }

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

    fun withShelf() = apply {
        shelf = true
    }

    fun registerIn(namespace: String): Unit = Registry(namespace).register()

    private inner class Registry(namespace: String) : PolyesterRegistry(namespace) {
        fun register() {
            if (post) registerBlock(PostBlock(material))
            if (platform) registerBlock(PlatformBlock(material))
            if (step) registerBlock(StepBlock(material))
            if (drawer) registerBlock(DrawerBlock(material.variantName))
            if (chair) {
                val block = registerBlock(ChairBlock(material.variantName))
                registerItem(ChairBlockItem(block))
            }
            if (table) {
                val block = registerBlock(TableBlock(material.variantName))
                registerItem(TableBlockItem(block))
            }
            if (kitchenCounter) registerBlock(KitchenCounterBlock(material.variantName))
            if (kitchenCupboard) registerBlock(KitchenCupboardBlock(material.variantName))
            if (shelf) registerBlock(ShelfBlock(material))
        }
    }

    companion object {
        @JvmStatic
        fun create(material: BlockVariant): AdornBlockBuilder =
            AdornBlockBuilder(material)
    }
}
