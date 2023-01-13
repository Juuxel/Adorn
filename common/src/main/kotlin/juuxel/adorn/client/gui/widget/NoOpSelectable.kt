package juuxel.adorn.client.gui.widget

import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder

abstract class NoOpSelectable : Selectable {
    override fun appendNarrations(builder: NarrationMessageBuilder) {
    }

    override fun getType(): Selectable.SelectionType =
        Selectable.SelectionType.NONE

    override fun isNarratable(): Boolean = false
}
