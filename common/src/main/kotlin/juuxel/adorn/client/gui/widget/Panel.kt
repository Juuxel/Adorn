package juuxel.adorn.client.gui.widget

import net.minecraft.client.gui.AbstractParentElement
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.util.math.MatrixStack

class Panel : AbstractParentElement(), Drawable, TickingElement {
    private val children: MutableList<Element> = ArrayList()
    override fun children() = children

    fun add(element: Element) {
        children += element
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        for (child in children) {
            if (child is Drawable) {
                child.render(matrices, mouseX, mouseY, delta)
            }
        }
    }

    override fun tick() {
        for (child in children) {
            if (child is TickingElement) {
                child.tick()
            }
        }
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double) =
        children.any { it.isMouseOver(mouseX, mouseY) }
}
