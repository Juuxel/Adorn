package juuxel.adorn.compat.modmenu

import io.github.cottonmc.cotton.gui.client.ClientCottonScreen
import io.github.prospector.modmenu.api.ModMenuApi
import juuxel.adorn.Adorn
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.LiteralText
import java.util.function.Function

object AdornModMenuPlugin : ModMenuApi {
    override fun getModId() = Adorn.NAMESPACE

    override fun getConfigScreenFactory(): Function<Screen, Screen> = Function { previous ->
        object : ClientCottonScreen(ConfigGui(previous)) {
            override fun onClose() {
                minecraft?.openScreen(previous)
            }
        }
    }
}
