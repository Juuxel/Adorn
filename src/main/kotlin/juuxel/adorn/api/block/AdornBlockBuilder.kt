package juuxel.adorn.api.block

import juuxel.adorn.api.util.BlockVariant
import juuxel.adorn.block.*
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.lib.RegistryHelper

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

    private inner class Registry(namespace: String) : RegistryHelper(namespace) {
        fun register() {
            val name = material.variantName
            if (post) registerBlock("${name}_post", PostBlock(material))
            if (platform) registerBlock("${name}_platform", PlatformBlock(material))
            if (step) registerBlock("${name}_step", StepBlock(material))
            if (drawer) registerBlock("${name}_drawer", DrawerBlock(material))
            if (chair) {
                val block = registerBlockWithoutItem("${name}_chair", ChairBlock(material))
                registerItem("${name}_chair", ChairBlockItem(block))
            }
            if (table) {
                val block = registerBlockWithoutItem("${name}_table", TableBlock(material))
                registerItem("${name}_table", TableBlockItem(block))
            }
            if (kitchenCounter) registerBlock("${name}_kitchen_counter", KitchenCounterBlock(material))
            if (kitchenCupboard) registerBlock("${name}_kitchen_cupboard", KitchenCupboardBlock(material))
            if (shelf) registerBlock("${name}_shelf", ShelfBlock(material))
        }
    }

    companion object {
        @JvmStatic
        fun create(material: BlockVariant): AdornBlockBuilder =
            AdornBlockBuilder(material)
    }
}
