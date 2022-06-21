package juuxel.adorn.compat.emi

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.SlotWidget
import juuxel.adorn.client.gui.screen.BrewerScreen
import juuxel.adorn.fluid.FluidUnit
import juuxel.adorn.fluid.FluidVolume
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.util.math.MatrixStack

class FluidSlotWidget(
    stack: EmiIngredient,
    x: Int, y: Int,
    private val width: Int, private val height: Int,
) : SlotWidget(stack, x, y) {
    private val stacks = stack.emiStacks.map {
        val entry = it.entry
        require(FluidVariant::class.java.isAssignableFrom(entry.type)) {
            "All stacks of ingredient $stack should be FluidVariants, but found $it with entry type ${entry.type}"
        }
        val variant = entry.value as FluidVariant
        FluidVolume(variant.fluid, it.amount, variant.nbt, FluidUnit.DROPLET)
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
