package juuxel.adorn.client.gui.widget

import juuxel.adorn.client.gui.forEach
import net.minecraft.client.gui.AbstractParentElement
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element

class Panel : AbstractParentElement(), Drawable, TickingElement, Draggable {
    private val children: MutableList<Element> = ArrayList()
    override fun children() = children

    fun add(element: Element) {
        children += element
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        forEach<Drawable> {
            it.render(context, mouseX, mouseY, delta)
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
