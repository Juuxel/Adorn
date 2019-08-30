package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.widget.Axis
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WSlider
import juuxel.adorn.block.entity.RgbLampBlockEntity
import juuxel.adorn.lib.ModNetworking
import juuxel.adorn.network.RgbLampColorUpdateC2SPacket
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

// TODO: Add a slot to store colors in a template
class RgbLampController(syncId: Int, playerInv: PlayerInventory, context: BlockContext, title: Text) :
    CottonScreenController(null, syncId, playerInv) {
    init {
        val root = WGridPanel()

        with(root) {
            add(WLabel(title, WLabel.DEFAULT_TEXT_COLOR), 0, 0)

            val be = BaseAdornController.getBlockEntity(context) as? RgbLampBlockEntity

            fun makeSlider(value: Int, valueChangeListener: (Int) -> Unit): WSlider =
                WSlider(MIN_SLIDER_VALUE, 255, Axis.VERTICAL).apply {
                    setValueChangeListener(valueChangeListener)
                    setDraggingFinishedListener listener@{
                        be ?: return@listener
                        ClientSidePacketRegistry.INSTANCE.sendToServer(
                            ModNetworking.RGB_LAMP_COLOR_UPDATE_C2S,
                            RgbLampColorUpdateC2SPacket(syncId, be.pos, be.red, be.green, be.blue).toBuf()
                        )
                        be.world?.updateListeners(be.pos, be.cachedState, be.cachedState, 4)
                    }
                    setValue(value)
                }

            add(makeSlider(be?.red ?: MIN_SLIDER_VALUE) { be?.red = it }, 1, 2, 1, 3)
            add(makeSlider(be?.green ?: MIN_SLIDER_VALUE) { be?.green = it }, 2, 2, 1, 3)
            add(makeSlider(be?.blue ?: MIN_SLIDER_VALUE) { be?.blue = it }, 3, 2, 1, 3)
        }

        setRootPanel(root)
        root.validate(this)
        root.setSize(5 * 18, root.height)
    }

    override fun canUse(entity: PlayerEntity?) = true

    companion object {
        const val MIN_SLIDER_VALUE: Int = 50
    }
}
