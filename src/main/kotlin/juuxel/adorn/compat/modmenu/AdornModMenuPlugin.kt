package juuxel.adorn.compat.modmenu

import io.github.prospector.modmenu.api.ModMenuApi
import juuxel.adorn.Adorn
import juuxel.adorn.config.AdornConfig
import me.sargunvohra.mcmods.autoconfig1.AutoConfig
import net.minecraft.client.gui.screen.Screen
import java.util.Optional

object AdornModMenuPlugin : ModMenuApi {
    override fun getModId() = Adorn.NAMESPACE
    override fun getConfigScreen(screen: Screen?) =
        Optional.of(AutoConfig.getConfigScreen(AdornConfig::class.java, screen))
}
