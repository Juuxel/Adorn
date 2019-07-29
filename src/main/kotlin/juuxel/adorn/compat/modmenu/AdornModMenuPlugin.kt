package juuxel.adorn.compat.modmenu

import io.github.cottonmc.cotton.gui.client.ClientCottonScreen
import io.github.prospector.modmenu.api.ModMenuApi
import juuxel.adorn.Adorn
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.Screen
import java.util.function.Function

@Environment(EnvType.CLIENT)
object AdornModMenuPlugin : ModMenuApi {
    override fun getModId() = Adorn.NAMESPACE

    override fun getConfigScreenFactory(): Function<Screen, Screen> = Function { previous ->
        val description = ConfigGui(previous)
        object : ClientCottonScreen(description) {
            override fun onClose() {
                description.close(previous)
            }
        }
    }
}
