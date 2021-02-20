package juuxel.adorn.api.block

import juuxel.adorn.block.AdornBlocks
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
import net.minecraft.block.Block
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderLayers
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

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

    fun register(modId: String, modBus: IEventBus) {
        val blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modId)
        val items = DeferredRegister.create(ForgeRegistries.ITEMS, modId)

        fun registerWithoutItem(suffix: String, block: () -> Block): Pair<String, RegistryObject<Block>> {
            val name = "${material.name}_$suffix"
            return name to blocks.register(name, block)
        }

        fun register(suffix: String, block: () -> Block) {
            val (name, blockDef) = registerWithoutItem(suffix, block)
            items.register(name) {
                AdornBlocks.makeItemForBlock(blockDef.get(), Item.Settings().group(ItemGroup.DECORATIONS))
            }
        }

        if (post) register("post") { PostBlock(material) }
        if (platform) register("platform") { PlatformBlock(material) }
        if (step) register("step") { StepBlock(material) }
        if (drawer) register("drawer") { DrawerBlock(material) }
        if (chair) {
            val (name, blockDef) = registerWithoutItem("chair") { ChairBlock(material) }
            items.register(name) { ChairBlockItem(blockDef.get()) }
        }
        if (table) {
            val (name, blockDef) = registerWithoutItem("table") { TableBlock(material) }
            items.register(name) { TableBlockItem(blockDef.get()) }
        }
        if (kitchenCounter) register("kitchen_counter") { KitchenCounterBlock(material) }
        if (kitchenCupboard) register("kitchen_cupboard") { KitchenCupboardBlock(material) }
        if (kitchenSink) register("kitchen_sink") { KitchenSinkBlock(material) }
        if (shelf) register("shelf") { ShelfBlock(material) }
        if (coffeeTable) register("coffee_table") { CoffeeTableBlock(material) }
        if (bench) register("bench") { BenchBlock(material) }

        blocks.register(modBus)
        items.register(modBus)

        DistExecutor.safeRunWhenOn(Dist.CLIENT) {
            DistExecutor.SafeRunnable {
                registerClient(modId, modBus)
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private fun registerClient(modId: String, modBus: IEventBus) {
        if (coffeeTable) {
            modBus.addListener<FMLClientSetupEvent> {
                val coffeeTableBlock = ForgeRegistries.BLOCKS.getValue(
                    Identifier(modId, "${material.name}_coffee_table")
                )

                RenderLayers.setRenderLayer(coffeeTableBlock, RenderLayer.getTranslucent())
            }
        }

        if (kitchenSink) {
            modBus.addListener<ColorHandlerEvent.Block> {
                val kitchenSinkBlock = ForgeRegistries.BLOCKS.getValue(
                    Identifier(modId, "${material.name}_kitchen_sink")
                )

                it.blockColors.registerColorProvider(SinkColorProvider, kitchenSinkBlock)
            }
        }
    }

    companion object {
        @JvmStatic
        fun create(material: BlockVariant): AdornBlockBuilder =
            AdornBlockBuilder(material)
    }
}
