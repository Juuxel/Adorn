package juuxel.adorn.compat.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.Screen

@Environment(EnvType.CLIENT)
object AdornModMenuPlugin : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<Screen> = ConfigScreenFactory { previous ->
        val description = ConfigScreenDescription(previous)
        object : CottonClientScreen(description) {
            override fun onClose() {
                description.close(previous)
            }
        }
    }
}
