package juuxel.adorn.compat.rei.client

import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.fluid.FluidVolume
import me.shedaniel.rei.api.client.gui.widgets.Widget
import net.minecraft.client.gui.Element
import net.minecraft.client.util.math.MatrixStack

class BrewerFluidWidget(private val x: Int, private val y: Int, private val fluids: List<FluidVolume>) : Widget() {
    override fun children(): List<Element> = emptyList()

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        if (fluids.isEmpty()) return

        val currentFluid = if (fluids.size == 1) {
            fluids.first()
        } else {
            val index = (System.currentTimeMillis() / 1000L) % fluids.size
            fluids[index.toInt()]
        }

        BrewerScreen.drawFluid(matrices, x, y, currentFluid)

        if (mouseX in x until (x + 16) && mouseY in y until (y + BrewerScreen.FLUID_AREA_HEIGHT)) {
            // TODO: actually draw the tooltip
        }
    }
}
