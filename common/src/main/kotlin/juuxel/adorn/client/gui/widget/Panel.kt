package juuxel.adorn.client.gui.widget

import juuxel.adorn.client.gui.forEach
import net.minecraft.client.gui.AbstractParentElement
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.util.math.MatrixStack

class Panel : AbstractParentElement(), Drawable, TickingElement, Draggable {
    private val children: MutableList<Element> = ArrayList()
    private val drawables: MutableList<Drawable> = ArrayList()
    override fun children() = children

    fun add(element: Element) {
        children += element
    }

    fun addDrawable(drawable: Drawable) {
        drawables += drawable
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        for (drawable in drawables) {
            drawable.render(matrices, mouseX, mouseY, delta)
        }

        forEach<Drawable> {
            it.render(matrices, mouseX, mouseY, delta)
        }
    }

    override fun tick() {
        forEach<TickingElement> {
            it.tick()
        }
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double) =
        children.any { it.isMouseOver(mouseX, mouseY) }

    override fun stopDragging() {
        forEach<Draggable> {
            it.stopDragging()
        }
    }
}
