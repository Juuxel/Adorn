package juuxel.adorn.compat.modmenu

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.ClientCottonScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.*
import juuxel.adorn.config.AdornConfigManager
import juuxel.adorn.gui.widget.WCenteredLabel
import juuxel.adorn.util.Colors
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.TranslatableText
import kotlin.reflect.KMutableProperty

class ConfigGui(previous: Screen) : LightweightGuiDescription() {
    init {
        val root = WPlainPanel()
        root.setBackgroundPainter(null)
        setRootPanel(root)

        root.add(
            WCenteredLabel(TranslatableText("text.autoconfig.Adorn.title"), Colors.WHITE),
            0, 0, 11 * 18, 18
        )

        root.add(WDisabledButton(TranslatableText("text.autoconfig.Adorn.category.general")), 0, 18 + 5, 4 * 18, 18)
        root.add(WButton(TranslatableText("text.autoconfig.Adorn.category.advanced")).apply {
            setOnClick {
                MinecraftClient.getInstance().openScreen(
                    object : ClientCottonScreen(Advanced(previous, MinecraftClient.getInstance().currentScreen!!)) {
                        override fun onClose() {
                            minecraft?.openScreen(previous)
                        }
                    }
                )
            }
        }, 4 * 18 + 4, 18 + 5, 4 * 18, 18)

        val main = WGridPanel()
        with(main) {
            val config = AdornConfigManager.CONFIG
            add(createConfigToggle(config::skipNightOnSofas), 0, 0)
            add(createConfigToggle(config::protectTradingStations), 0, 1)
            add(createConfigToggle(config::sittingOnTables), 0, 2)

            add(WButton(TranslatableText("gui.done")).apply {
                setOnClick {
                    MinecraftClient.getInstance().openScreen(previous)
                }
            }, 0, 4, 3, 1)

            setBackgroundPainter(BackgroundPainter.VANILLA)
        }

        root.add(main, 0, 48 + 5, 11 * 18, main.height)
        root.validate(this)
    }

    private class Advanced(previous: Screen, general: Screen) : LightweightGuiDescription() {
        init {
            val root = WPlainPanel()
            root.setBackgroundPainter(null)
            setRootPanel(root)

            root.add(
                WCenteredLabel(TranslatableText("text.autoconfig.Adorn.title"), Colors.WHITE),
                0, 0, 11 * 18, 18
            )

            root.add(WButton(TranslatableText("text.autoconfig.Adorn.category.general")).apply {
                setOnClick {
                    MinecraftClient.getInstance().openScreen(general)
                }
            }, 0, 18 + 5, 4 * 18, 18)
            root.add(WDisabledButton(TranslatableText("text.autoconfig.Adorn.category.advanced")), 4 * 18 + 4, 18 + 5, 4 * 18, 18)

            val main = WGridPanel()
            with(main) {
                val config = AdornConfigManager.CONFIG
                add(createConfigToggle(config::enableOldStoneRods), 0, 0)

                add(WButton(TranslatableText("gui.done")).apply {
                    setOnClick {
                        MinecraftClient.getInstance().openScreen(previous)
                    }
                }, 0, 4, 3, 1)

                setBackgroundPainter(BackgroundPainter.VANILLA)
            }

            root.add(main, 0, 48 + 5, 11 * 18, main.height)
            root.validate(this)
        }
    }

    companion object {
        private fun createConfigToggle(property: KMutableProperty<Boolean>): WToggleButton =
            object : WToggleButton(TranslatableText("text.autoconfig.Adorn.option.${property.name}")) {
                init {
                    setToggle(property.getter.call())
                }

                override fun onToggle(on: Boolean) {
                    property.setter.call(on)
                    AdornConfigManager.save()
                }

                override fun addInformation(information: MutableList<String>) {
                    information += TranslatableText("text.autoconfig.Adorn.option.${property.name}.@Tooltip")
                        .asFormattedString()
                }
            }
    }
}
