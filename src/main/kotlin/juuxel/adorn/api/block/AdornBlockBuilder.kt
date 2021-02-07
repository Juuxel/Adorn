package juuxel.adorn.api.block

import juuxel.adorn.block.BenchBlock
import juuxel.adorn.block.ChairBlock
import juuxel.adorn.block.CoffeeTableBlock
import juuxel.adorn.block.DrawerBlock
import juuxel.adorn.block.KitchenCounterBlock
import juuxel.adorn.block.KitchenCupboardBlock
import juuxel.adorn.block.KitchenSinkBlock
import juuxel.adorn.block.PlatformBlock
import juuxel.adorn.block.PostBlock
import juuxel.adorn.block.ShelfBlock
import juuxel.adorn.block.StepBlock
import juuxel.adorn.block.TableBlock
import juuxel.adorn.client.SinkColorProvider
import juuxel.adorn.item.ChairBlockItem
import juuxel.adorn.item.TableBlockItem
import juuxel.adorn.lib.RegistryHelper
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry as GameRegistry

class AdornBlockBuilder private constructor(private val material: BlockVariant) {
    private var post = false
    private var platform = false
    private var step = false
    private var drawer = false
    private var chair = false
    private var table = false
    private var kitchenCounter = false
    private var kitchenCupboard = false
    private var kitchenSink = false
    private var shelf = false
    private var coffeeTable = false
    private var bench = false

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

    fun withKitchenSink() = apply {
        kitchenSink = true
    }

    fun withKitchenBlocks() = apply {
        kitchenCounter = true
        kitchenCupboard = true
        kitchenSink = true
    }

    fun withShelf() = apply {
        shelf = true
    }

    fun withCoffeeTable() = apply {
        coffeeTable = true
    }

    fun withBench() = apply {
        bench = true
    }

    fun withEverything() = apply {
        post = true
        platform = true
        step = true
        drawer = true
        chair = true
        table = true
        kitchenCounter = true
        kitchenCupboard = true
        kitchenSink = true
        shelf = true
        coffeeTable = true
        bench = true
    }

    fun registerIn(namespace: String): Unit = Registry(namespace).register()

    private inner class Registry(private val namespace: String) : RegistryHelper(namespace) {
        fun register() {
            val name = material.name
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
            if (kitchenSink) registerBlock("${name}_kitchen_sink", KitchenSinkBlock(material))
            if (shelf) registerBlock("${name}_shelf", ShelfBlock(material))
            if (coffeeTable) registerBlock("${name}_coffee_table", CoffeeTableBlock(material))
            if (bench) registerBlock("${name}_bench", BenchBlock(material))

            if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
                registerClient()
            }
        }

        @Environment(EnvType.CLIENT)
        private fun registerClient() {
            fun block(type: String) =
                GameRegistry.BLOCK[Identifier(namespace, "${material.name}_$type")]

            val sink = block("kitchen_sink")
            ColorProviderRegistry.BLOCK.register(SinkColorProvider, sink)
            val coffeeTable = block("coffee_table")
            BlockRenderLayerMap.INSTANCE.putBlock(coffeeTable, RenderLayer.getTranslucent())
        }
    }

    companion object {
        @JvmStatic
        fun create(material: BlockVariant): AdornBlockBuilder =
            AdornBlockBuilder(material)
    }
}
