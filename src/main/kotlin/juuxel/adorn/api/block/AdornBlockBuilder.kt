package juuxel.adorn.api.block

import juuxel.adorn.api.util.BlockVariant
import juuxel.adorn.block.*
import juuxel.adorn.item.AdornTallBlockItem
import juuxel.polyester.registry.PolyesterRegistry
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

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
            if (drawer) registerBlock(DrawerBlock(material))
            if (chair) {
                val block = registerBlock(ChairBlock(material))
                registerItem(AdornTallBlockItem(block, Item.Settings().group(ItemGroup.DECORATIONS)))
            }
            if (table) registerBlock(TableBlock(material))
            if (kitchenCounter) registerBlock(KitchenCounterBlock(material))
            if (kitchenCupboard) registerBlock(KitchenCupboardBlock(material))
            if (shelf) registerBlock(ShelfBlock(material))
        }
    }

    companion object {
        @JvmStatic
        fun create(material: BlockVariant): AdornBlockBuilder =
            AdornBlockBuilder(material)
    }
}
