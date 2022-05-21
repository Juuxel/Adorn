package juuxel.adorn.client.gui.screen

import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.Colors
import net.minecraft.client.gui.screen.NoticeScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.CyclingButtonWidget
import net.minecraft.client.util.OrderableTooltip
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.reflect.KMutableProperty

abstract class AbstractConfigScreen(title: Text, private val parent: Screen) : Screen(title) {
    private var restartRequired = false

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        drawCenteredText(matrices, textRenderer, title, width / 2, 20, Colors.WHITE)
        super.render(matrices, mouseX, mouseY, delta)

        for (child in children()) {
            if (child.isMouseOver(mouseX.toDouble(), mouseY.toDouble()) && child is OrderableTooltip) {
                renderOrderedTooltip(matrices, child.orderedTooltip, mouseX, mouseY)
                break
            }
        }
    }

    override fun close() {
        client!!.setScreen(
            if (restartRequired) {
                NoticeScreen(
                    { client!!.setScreen(parent) },
                    Text.translatable("gui.adorn.config.restart_required.title"),
                    Text.translatable("gui.adorn.config.restart_required.message"),
                    Text.translatable("gui.ok")
                )
            } else {
                parent
            }
        )
    }

    private fun wrapTooltipLines(text: Text) =
        textRenderer.wrapLines(text, 200)

    protected fun createConfigToggle(
        x: Int, y: Int, width: Int, property: KMutableProperty<Boolean>, restartRequired: Boolean = false
    ): CyclingButtonWidget<Boolean> = CyclingButtonWidget.onOffBuilder(property.getter.call())
        .tooltip {
            buildList {
                addAll(wrapTooltipLines(Text.translatable(getTooltipTranslationKey(property.name))))

                if (restartRequired) {
                    addAll(
                        wrapTooltipLines(
                            Text.translatable("gui.adorn.config.requires_restart").formatted(Formatting.ITALIC, Formatting.GOLD)
                        )
                    )
                }
            }
        }
        .build(x, y, width, 20, Text.translatable(getOptionTranslationKey(property.name))) { _, value ->
            property.setter.call(value)
            PlatformBridges.configManager.save()

            if (restartRequired) {
                this.restartRequired = true
            }
        }

    protected open fun getOptionTranslationKey(name: String): String =
        "gui.adorn.config.option.$name"

    private fun getTooltipTranslationKey(name: String): String =
        "${getOptionTranslationKey(name)}.description"

    companion object {
        const val BUTTON_HEIGHT = 20
        const val BUTTON_GAP = 4
        const val BUTTON_SPACING = BUTTON_HEIGHT + BUTTON_GAP
    }
}
