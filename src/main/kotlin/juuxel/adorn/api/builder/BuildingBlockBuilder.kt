package juuxel.adorn.api.builder

import juuxel.adorn.api.util.BlockVariant
import juuxel.adorn.block.PlatformBlock
import juuxel.adorn.block.PostBlock
import juuxel.adorn.block.StepBlock
import juuxel.polyester.registry.PolyesterRegistry
import org.apiguardian.api.API

/**
 * @since 1.1.0
 */
@API(status = API.Status.EXPERIMENTAL, since = "1.1.0")
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

    fun register(namespace: String): Unit = Registry(namespace).register()

    private inner class Registry(namespace: String) : PolyesterRegistry(namespace) {
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
