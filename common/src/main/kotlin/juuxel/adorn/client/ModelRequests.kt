package juuxel.adorn.client

import juuxel.adorn.client.renderer.FridgeRenderer
import net.minecraft.util.Identifier
import java.util.function.Consumer

object ModelRequests {
    fun requestModels(out: Consumer<Identifier>) {
        out.accept(FridgeRenderer.LEFT_DOOR_MODEL)
        out.accept(FridgeRenderer.RIGHT_DOOR_MODEL)
    }
}
