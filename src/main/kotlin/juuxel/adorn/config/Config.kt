package juuxel.adorn.config

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import net.minecraftforge.common.ForgeConfigSpec
import java.nio.file.Path

object Config {
    val SHOW_ITEMS_IN_STANDARD_GROUPS: ForgeConfigSpec.ConfigValue<Boolean>

    private val CONFIG: ForgeConfigSpec

    init {
        val builder = ForgeConfigSpec.Builder()
        SHOW_ITEMS_IN_STANDARD_GROUPS =
            builder.comment("If true, Adorn items will also be shown in matching vanilla item tabs.")
                .define("show-items-in-standard-groups", true)

        CONFIG = builder.build()
    }

    fun load(path: Path) {
        val configData = CommentedFileConfig.builder(path)
            .autosave()
            .build()

        CONFIG.setConfig(configData)
    }
}
