package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.InventoryComponent
import juuxel.adorn.util.containsOldUuid
import juuxel.adorn.util.getOldUuid
import juuxel.adorn.util.getText
import juuxel.adorn.util.putText
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.menu.MenuContext
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import java.util.UUID

class TradingStationBlockEntity : BlockEntity(
    AdornBlockEntities.TRADING_STATION.get()
), NamedMenuFactory, TradingStation {
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
        TradingStationMenu(syncId, playerInv, MenuContext.create(world, pos))

    override fun getDisplayName() = TranslatableText(cachedState.block.translationKey)

    // NBT

    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)

        if (tag.containsUuid(NBT_TRADING_OWNER)) {
            owner = tag.getUuid(NBT_TRADING_OWNER)
        } else if (tag.containsOldUuid(NBT_TRADING_OWNER)) {
            owner = tag.getOldUuid(NBT_TRADING_OWNER)
        }

        ownerName = tag.getText(NBT_TRADING_OWNER_NAME) ?: LiteralText("??")
        trade.fromTag(tag.getCompound(NBT_TRADE))
        storage.fromTag(tag.getCompound(NBT_STORAGE))
    }

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        if (owner != null) {
            tag.putUuid(NBT_TRADING_OWNER, owner)
        }

        tag.putText(NBT_TRADING_OWNER_NAME, ownerName)

        tag.put(NBT_TRADE, trade.toTag(CompoundTag()))
        tag.put(NBT_STORAGE, storage.toTag(CompoundTag()))
    }

    // Client NBT

    override fun toInitialChunkDataTag() = toTag(CompoundTag())

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket {
        val tag = CompoundTag()
        tag.putText(NBT_TRADING_OWNER_NAME, ownerName)
        tag.put(NBT_TRADE, trade.toTag(CompoundTag()))
        return BlockEntityUpdateS2CPacket(pos, -1, tag)
    }

    override fun onDataPacket(net: ClientConnection, packet: BlockEntityUpdateS2CPacket) {
        val tag = packet.compoundTag
        trade.fromTag(tag.getCompound(NBT_TRADE))
        ownerName = tag.getText(NBT_TRADING_OWNER_NAME) ?: return
    }

    companion object {
        const val NBT_TRADING_OWNER = "TradingOwner"
        const val NBT_TRADING_OWNER_NAME = "TradingOwnerName"
        const val NBT_TRADE = "Trade"
        const val NBT_STORAGE = "Storage"
    }
}
