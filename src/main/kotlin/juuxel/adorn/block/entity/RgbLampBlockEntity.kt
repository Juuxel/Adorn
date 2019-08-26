package juuxel.adorn.block.entity

import juuxel.adorn.block.RgbLampBlock
import juuxel.adorn.gui.controller.RgbLampController
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.container.BlockContext
import net.minecraft.container.NameableContainerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

@UseExperimental(ExperimentalUnsignedTypes::class)
class RgbLampBlockEntity : BlockEntity(RgbLampBlock.BLOCK_ENTITY_TYPE), BlockEntityClientSerializable,
    NameableContainerProvider {
    var red: UByte = 50U
    var green: UByte = 50U
    var blue: UByte = 50U

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity) =
        RgbLampController(syncId, playerInv, BlockContext.create(world, pos), displayName)

    override fun getDisplayName(): Text = TranslatableText(cachedState.block.translationKey)

    // Tag (de)serialization
    private fun writeColorToTag(): CompoundTag = CompoundTag().apply {
        putByte("Red", red.toByte())
        putByte("Green", green.toByte())
        putByte("Blue", blue.toByte())
    }

    private fun readColorFromTag(tag: CompoundTag) = with(tag) {
        red = getByte("Red").toUByte()
        green = getByte("Green").toUByte()
        blue = getByte("Blue").toUByte()
    }

    override fun toClientTag(tag: CompoundTag) =
        tag.apply {
            put("Color", writeColorToTag())
        }

    override fun fromClientTag(tag: CompoundTag) {
        readColorFromTag(tag.getTag("Color") as? CompoundTag ?: return)
    }
    override fun toTag(tag: CompoundTag): CompoundTag =
        super.toTag(tag).apply {
            tag.put("Color", writeColorToTag())
        }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        readColorFromTag(tag.getTag("Color") as? CompoundTag ?: return)
    }
}
