package juuxel.adorn.gui.screen

import io.github.cottonmc.cotton.gui.GuiDescription
import io.github.cottonmc.cotton.gui.client.ClientCottonScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.Tickable

// TODO: Remove in 1.15
@Environment(EnvType.CLIENT)
class TickingClientScreen<D>(description: D) : ClientCottonScreen(description) where D : GuiDescription, D : Tickable {
    override fun tick() {
        super.tick()
        (description as Tickable).tick()
    }
}
