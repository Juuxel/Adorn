package juuxel.adorn.gui.controller

import io.github.cottonmc.cotton.gui.CottonScreenController
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.netty.buffer.Unpooled
import juuxel.adorn.block.entity.RgbLampBlockEntity
import juuxel.adorn.gui.widget.WSlider
import juuxel.adorn.lib.ModNetworking
import juuxel.adorn.network.RgbLampColorUpdateC2SPacket
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.container.BlockContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.PacketByteBuf

@UseExperimental(ExperimentalUnsignedTypes::class)
class RgbLampController(syncId: Int, playerInv: PlayerInventory, context: BlockContext, title: Text) :
    CottonScreenController(null, syncId, playerInv) {
    init {
        val root = WGridPanel()

        with(root) {
            add(WLabel(title, WLabel.DEFAULT_TEXT_COLOR), 0, 0)

            val be = BaseAdornController.getBlockEntity(context) as? RgbLampBlockEntity

            fun makeSlider(valueChangeListener: (Int) -> Unit) =
                WSlider(MIN_SLIDER_VALUE, 255).setValueChangeListener(valueChangeListener).setMouseReleaseListener listener@{
                    be ?: return@listener
                    ClientSidePacketRegistry.INSTANCE.sendToServer(
                        ModNetworking.RGB_LAMP_COLOR_UPDATE,
                        RgbLampColorUpdateC2SPacket(syncId, be.pos, be.red, be.green, be.blue)
                            .write(PacketByteBuf(Unpooled.buffer()))
                    )
                    be.world?.updateListeners(be.pos, be.cachedState, be.cachedState, 4)
                }

            add(makeSlider { be?.red = it.toUByte() }.setValue(be?.red?.toInt() ?: MIN_SLIDER_VALUE), 1, 2, 1, 3)
            add(makeSlider { be?.green = it.toUByte() }.setValue(be?.green?.toInt() ?: MIN_SLIDER_VALUE), 2, 2, 1, 3)
            add(makeSlider { be?.blue = it.toUByte() }.setValue(be?.blue?.toInt() ?: MIN_SLIDER_VALUE), 3, 2, 1, 3)
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
