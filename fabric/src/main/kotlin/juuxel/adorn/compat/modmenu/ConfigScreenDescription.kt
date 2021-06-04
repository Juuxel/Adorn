package juuxel.adorn.compat.modmenu

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WPlainPanel
import io.github.cottonmc.cotton.gui.widget.WTabPanel
import io.github.cottonmc.cotton.gui.widget.WToggleButton
import io.github.cottonmc.cotton.gui.widget.WWidget
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment
import io.github.cottonmc.cotton.gui.widget.data.Insets
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon
import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.util.Colors
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.NoticeScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import kotlin.reflect.KMutableProperty

@Environment(EnvType.CLIENT)
class ConfigScreenDescription(previous: Screen) : LightweightGuiDescription() {
    internal var restartRequired: Boolean = false

    init {
        val root = WPlainPanel()
        setRootPanel(root)

        root.add(
            WLabel(TranslatableText("gui.adorn.config.title"), Colors.WHITE).setHorizontalAlignment(
                HorizontalAlignment.CENTER
            ),
            0, 0, 12 * 18, 18
        )

        val tabbed = WTabPanel()
        val config = ConfigManager.CONFIG

        val general = WGridPanel().setInsets(Insets.ROOT_PANEL)
        with(general) {
            add(createConfigToggle(config::protectTradingStations), 0, 0)
            add(createConfigToggle(config.client::showTradingStationTooltips), 0, 1)
            add(createConfigToggle(config.client::showItemsInStandardGroups), 0, 2)

            backgroundPainter = BackgroundPainter.VANILLA
            setSize(12 * 18, height)
        }

        val gameRules = WGridPanel().setInsets(Insets.ROOT_PANEL)
        with(gameRules) {
            add(createConfigToggle(config.gameRuleDefaults::skipNightOnSofas), 0, 0)

            backgroundPainter = BackgroundPainter.VANILLA
            setSize(12 * 18, height)
        }

        tabbed.add(general) {
            it.icon(ItemIcon(ItemStack(AdornBlocks.OAK_TABLE)))
            it.tooltip(TranslatableText("gui.adorn.config.category.general"))
        }
        tabbed.add(gameRules) {
            it.icon(ItemIcon(ItemStack(Items.PAPER)))
            it.tooltip(TranslatableText("gui.adorn.config.category.game_rules"))
        }

        root.add(tabbed, 0, 18 + 5, tabbed.width, tabbed.height)
        root.add(
            WButton(TranslatableText("gui.done")).apply {
                setOnClick { close(previous) }
            },
            12 * 9 - 5 * 9, 8 * 18, 5 * 18, 18
        )
        root.validate(this)
    }

    override fun addPainters() {
        // Overridden to disable the default root panel painter
    }

    fun close(previous: Screen) {
        if (restartRequired) {
            MinecraftClient.getInstance().openScreen(
                NoticeScreen(
                    { MinecraftClient.getInstance().openScreen(previous) },
                    TranslatableText("gui.adorn.config.restart_required.title"),
                    TranslatableText("gui.adorn.config.restart_required.message"),
                    TranslatableText("gui.ok")
                )
            )
        } else {
            MinecraftClient.getInstance().openScreen(previous)
        }
    }

    private fun createConfigToggle(property: KMutableProperty<Boolean>, restartRequired: Boolean = false): WWidget =
        object : WToggleButton(TranslatableText("gui.adorn.config.option.${property.name}")) {
            init {
                toggle = property.getter.call()
            }

            override fun onToggle(on: Boolean) {
                property.setter.call(on)
                ConfigManager.save()

                if (restartRequired) {
                    this@ConfigScreenDescription.restartRequired = true
                }
            }

            override fun addTooltip(tooltip: TooltipBuilder) {
                tooltip.add(TranslatableText("gui.adorn.config.option.${property.name}.tooltip"))

                if (restartRequired) {
                    tooltip.add(
                        TranslatableText("gui.adorn.config.requires_restart")
                            .formatted(Formatting.ITALIC, Formatting.GOLD)
                    )
                }
            }
        }
}
