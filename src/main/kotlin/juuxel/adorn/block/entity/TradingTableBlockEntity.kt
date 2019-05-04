package juuxel.adorn.block.entity

import juuxel.adorn.block.TradingTableBlock
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.InventoryComponent
import juuxel.adorn.util.getTextComponent
import juuxel.adorn.util.putTextComponent
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.HopperBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.StringTextComponent
import net.minecraft.text.TextComponent
import java.util.UUID

class TradingTableBlockEntity : BlockEntity(TradingTableBlock.BLOCK_ENTITY_TYPE), BlockEntityClientSerializable {
    var owner: UUID? = null
    var ownerName: TextComponent = StringTextComponent("???")
    val trade: Trade = Trade(ItemStack.EMPTY, ItemStack.EMPTY)
    val storage: InventoryComponent = InventoryComponent(12)

    fun setOwner(player: PlayerEntity) {
        owner = player.gameProfile.id
        ownerName = StringTextComponent(player.gameProfile.name)
        markDirty()
    }

    fun isStorageStocked(): Boolean =
        storage.getInvAmountOf(trade.selling.item) >= trade.selling.amount

    // NBT

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        owner = tag.getUuid(NBT_TRADING_OWNER)
        ownerName = tag.getTextComponent(NBT_TRADING_OWNER_NAME) ?: StringTextComponent("??")
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
