package juuxel.adorn.compat.emi

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.SlotWidget
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.FluidVolume
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.fluid.Fluid

class FluidSlotWidget(
    stack: EmiIngredient,
    x: Int, y: Int,
    private val width: Int, private val height: Int
) : SlotWidget(stack, x, y) {
    private val stacks = stack.emiStacks.mapNotNull {
        if (it.isEmpty) return@mapNotNull null
        val fluid = it.getKeyOfType(Fluid::class.java)
            ?: throw IllegalArgumentException("All stacks of ingredient $stack should have fluid keys, but found $it with key ${it.key}")
        FluidVolume(fluid, it.amount, it.nbt, FluidUnit.DROPLET)
    }

    override fun getBounds(): Bounds = Bounds(x, y, width, height)

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        if (stacks.isEmpty()) return
        val currentStackIndex = (System.currentTimeMillis() / 1000) % stacks.size
        val currentStack = stacks[currentStackIndex.toInt()]
        BrewerScreen.drawFluid(matrices, x + 1, y + 1, currentStack)

        // TODO: Highlights?
    }
}
