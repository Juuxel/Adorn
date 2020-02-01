package juuxel.adorn.block.entity

import java.util.UUID
import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.gui.controller.TradingStationController
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.InventoryComponent
import juuxel.adorn.util.getTextComponent
import juuxel.adorn.util.putTextComponent
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.container.BlockContext
import net.minecraft.container.NameableContainerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class TradingStationBlockEntity : BlockEntity(AdornBlockEntities.TRADING_STATION), BlockEntityClientSerializable, NameableContainerProvider, TradingStation {
    var owner: UUID? = null
    var ownerName: Text = LiteralText("???")
    override val trade: Trade = Trade(ItemStack.EMPTY, ItemStack.EMPTY)
    override val storage: InventoryComponent = InventoryComponent(12)

    init {
        trade.addListener {
            markDirty()
        }

        storage.addListener {
            markDirty()
        }
    }

    fun setOwner(player: PlayerEntity) {
        owner = player.gameProfile.id
        ownerName = LiteralText(player.gameProfile.name)
        markDirty()
    }

    fun isStorageStocked(): Boolean =
        storage.getAmountWithNbt(trade.selling) >= trade.selling.count

    fun isOwner(player: PlayerEntity) = player.gameProfile.id == owner

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity) =
        TradingStationController(syncId, playerInv, BlockContext.create(world, pos), isOwner(player))

    override fun getDisplayName() = TranslatableText(cachedState.block.translationKey)

    // NBT

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        owner = tag.getUuid(NBT_TRADING_OWNER)
        ownerName = tag.getTextComponent(NBT_TRADING_OWNER_NAME) ?: LiteralText("??")
        trade.fromTag(tag.getCompound(NBT_TRADE))
        storage.fromTag(tag.getCompound(NBT_STORAGE))
    }

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        if (owner != null) {
            tag.putUuid(NBT_TRADING_OWNER, owner)
        }

        tag.putTextComponent(NBT_TRADING_OWNER_NAME, ownerName)

        tag.put(NBT_TRADE, trade.toTag(CompoundTag()))
        tag.put(NBT_STORAGE, storage.toTag(CompoundTag()))
    }

    // Client NBT

    override fun toClientTag(tag: CompoundTag) = tag.apply {
        putTextComponent(NBT_TRADING_OWNER_NAME, ownerName)
        put(NBT_TRADE, trade.toTag(CompoundTag()))
    }

    override fun fromClientTag(tag: CompoundTag) {
        trade.fromTag(tag.getCompound(NBT_TRADE))
        ownerName = tag.getTextComponent(NBT_TRADING_OWNER_NAME) ?: return
    }

    companion object {
        const val NBT_TRADING_OWNER = "TradingOwner"
        const val NBT_TRADING_OWNER_NAME = "TradingOwnerName"
        const val NBT_TRADE = "Trade"
        const val NBT_STORAGE = "Storage"
    }
}
