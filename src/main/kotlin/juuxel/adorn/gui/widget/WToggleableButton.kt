package juuxel.adorn.gui.widget

import io.github.cottonmc.cotton.gui.widget.WButton
import net.minecraft.text.Text

/**
 * Toggleable and groupable buttons.
 * Can be grouped with [setGroup] and a [Group] instance.
 */
class WToggleableButton(private val label: Text) : WButton(label) {
    private var group: Group? = null
    private var onClick: Runnable? = null

    init {
        super.setOnClick {
            group?.buttons?.forEach { it.isEnabled = true }
            isEnabled = false
            onClick?.run()
        }
    }

    fun setGroup(group: Group) {
        require(this.group == null) { "Group has already been set" }
        this.group = group

        if (group.buttons.none { !it.isEnabled })
            this.isEnabled = false

        group.buttons += this
    }

    override fun setOnClick(r: Runnable): WButton = apply {
        this.onClick = r
    }

    inline fun setOnClick(crossinline fn: () -> Unit) =
        setOnClick(Runnable { fn() })

    /**
     * A group of [toggleable buttons][WToggleableButton].
     */
    class Group {
        internal val buttons: MutableList<WToggleableButton> = ArrayList()
    }
}
