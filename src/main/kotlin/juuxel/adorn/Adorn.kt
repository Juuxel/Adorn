package juuxel.adorn

import juuxel.adorn.config.AdornConfigManager
import juuxel.adorn.lib.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object Adorn : ModInitializer {
    const val NAMESPACE = "adorn"
    private var runTasksImmediately = false
    private val tasks = ArrayList<() -> Unit>()

    override fun onInitialize() {
        AdornConfigManager.init()
        ModBlocks.init()
        ModItems.init()
        ModEntities.init()
        ModGuis.init()
        ModTags.init()
        AdornCompat.init()

        // Run post-init tasks
        tasks.forEach { it() }
        tasks.clear()
        runTasksImmediately = true
    }

    @Environment(EnvType.CLIENT)
    @Suppress("UNUSED")
    fun initClient() {
        ModBlocks.initClient()
        ModGuis.initClient()
        ModNetworking.initClient()
        AdornResources.initClient()
    }

    fun id(path: String) = Identifier(NAMESPACE, path)

    internal fun runTaskAfterInit(task: () -> Unit) {
        if (runTasksImmediately) task()
        else tasks += task
    }
}
