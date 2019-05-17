package juuxel.adorn.block.builder

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.block.PlatformBlock
import juuxel.adorn.block.PostBlock
import juuxel.adorn.block.StepBlock
import juuxel.adorn.util.BlockVariant

/**
 * @since 0.2.0
 */
class BuildingBlockBuilder private constructor(private val material: BlockVariant) {
    private var post = false
    private var platform = false
    private var step = false

    fun withPost() = apply {
        post = true
    }

    fun withPlatform() = apply {
        platform = true
    }

    fun withStep() = apply {
        step = true
    }

    fun register(): Unit = Registry().register()

    private inner class Registry : PolyesterRegistry(material.id.namespace) {
        fun register() {
            if (post) registerBlock(PostBlock(material))
            if (platform) registerBlock(PlatformBlock(material))
            if (step) registerBlock(StepBlock(material))
        }
    }

    companion object {
        @JvmStatic
        fun create(material: BlockVariant): BuildingBlockBuilder =
            BuildingBlockBuilder(material)
    }
}
