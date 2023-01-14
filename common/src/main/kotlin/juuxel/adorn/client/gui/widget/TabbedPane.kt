package juuxel.adorn.client.gui.widget

import com.mojang.blaze3d.systems.RenderSystem
import juuxel.adorn.AdornCommon
import juuxel.adorn.client.gui.NinePatchRenderer
import juuxel.adorn.client.resources.BlockVariantIcon
import juuxel.libninepatch.NinePatch
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.gui.widget.PressableWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class TabbedPane<E : Element> private constructor(
    val flipBook: FlipBook<E>,
    private val panel: Panel,
    private val tabs: List<Tab<E>>
    ) : WidgetEnvelope() {
    override fun current(): Element = panel

    fun currentTab(): Tab<E>? =
        tabs.find { it.element === flipBook.currentPageValue }

    class Builder<E : Element>(private val x: Int, private val y: Int, private val width: Int, private val height: Int) {
        private val tabs: MutableList<Tab<E>> = ArrayList()
        private var topBottomTabs = false

        fun topBottomTabs(): Builder<E> {
            topBottomTabs = true
            return this
        }

        fun tab(icon: TabIcon, label: Text, element: (x: Int, y: Int) -> E): Builder<E> =
            tab(icon, label, element(x + HORIZONTAL_PADDING, y + VERTICAL_PADDING))

        fun tab(icon: TabIcon, label: Text, element: E): Builder<E> {
            tabs += Tab(icon, label, element)
            return this
        }

        fun build(): TabbedPane<E> {
            val panel = Panel()
            val tabbedPane = TabbedPane(FlipBook(), panel, tabs)
            val tabsPerRow = 1 + (width - TAB_WIDTH) / (TAB_WIDTH + 1)
            val hasBottomRow = topBottomTabs && tabs.size > tabsPerRow

            panel.add(tabbedPane.flipBook)
            panel.addDrawable { matrices, _, _, _ ->
                val backgroundHeight = if (hasBottomRow) {
                    height - 2 * TAB_HEIGHT + 8
                } else {
                    height - TAB_HEIGHT + 4
                }

                matrices.push()
                matrices.translate(x.toFloat(), (y + TAB_HEIGHT - 4).toFloat(), 0f)
                NINE_PATCH.draw(NinePatchRenderer, matrices, width, backgroundHeight)
                matrices.pop()
            }

            for ((index, tab) in tabs.withIndex()) {
                val onBottomRow = topBottomTabs && index >= tabsPerRow
                val indexOnRow = if (onBottomRow) {
                    index - tabsPerRow
                } else {
                    index
                }
                val tabX = indexOnRow * (TAB_WIDTH + 1)
                val tabY = if (onBottomRow) {
                    height - TAB_HEIGHT
                } else {
                    0
                }
                val style = when {
                    indexOnRow == 0 -> 0
                    tabX + TAB_WIDTH == width -> 2
                    else -> 1
                }
                panel.add(tabbedPane.TabButton(x + tabX, y + tabY, style, tab, onBottomRow))
                tabbedPane.flipBook.add(tab.element)
            }

            return tabbedPane
        }
    }

    companion object {
        private const val HORIZONTAL_PADDING = 7
        private const val VERTICAL_PADDING = 35
        private const val TAB_WIDTH = 28
        const val TAB_HEIGHT = 32
        private const val ICON_X = 6
        private const val ICON_Y = 9
        private val NINE_PATCH = NinePatch.builder(AdornCommon.id("textures/gui/tabbed_pane.png"))
            .cornerSize(4)
            .cornerUv(0.25f)
            .build()
        private val TAB_TEXTURE = Identifier("minecraft", "textures/gui/advancements/tabs.png")

        inline operator fun <E : Element> invoke(x: Int, y: Int, width: Int, height: Int, configurator: Builder<E>.() -> Unit): TabbedPane<E> =
            Builder<E>(x, y, width, height).apply(configurator).build()

        fun iconOf(icon: BlockVariantIcon): TabIcon =
            TabIcon { matrices, x, y ->
                icon.render(matrices, x, y, MinecraftClient.getInstance().itemRenderer)
            }
    }

    class Tab<E : Element>(val icon: TabIcon, val label: Text, val element: E)

    private inner class TabButton(
        x: Int, y: Int,
        private val style: Int,
        private val tab: Tab<E>,
        private val bottom: Boolean
    ) : PressableWidget(x, y, TAB_WIDTH, TAB_HEIGHT, tab.label) {
        init {
            setTooltip(Tooltip.of(tab.label))
        }

        override fun renderButton(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            var u = style * TAB_WIDTH
            if (bottom) u += 84
            val v = if (flipBook.currentPageValue === tab.element) TAB_HEIGHT else 0
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
            RenderSystem.setShaderTexture(0, TAB_TEXTURE)
            drawTexture(matrices, x, y, u, v, TAB_WIDTH, TAB_HEIGHT)
            val iconY = if (bottom) {
                height - ICON_Y - 16
            } else {
                ICON_Y
            }
            tab.icon.render(matrices, x + ICON_X, y + iconY)
        }

        override fun appendClickableNarrations(builder: NarrationMessageBuilder) {
            appendDefaultNarrations(builder)
        }

        override fun onPress() {
            flipBook.showPage(tab.element)
        }
    }

    fun interface TabIcon {
        fun render(matrices: MatrixStack, x: Int, y: Int)
    }
}
