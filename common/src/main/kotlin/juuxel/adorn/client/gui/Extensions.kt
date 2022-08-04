package juuxel.adorn.client.gui

import net.minecraft.client.gui.ParentElement

inline fun <reified T> ParentElement.forEach(code: (T) -> Unit) {
    for (child in children()) {
        if (child is T) {
            code(child)
        }
    }
}
