package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.gui.controller.TradingStationController
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.InventoryComponent
import juuxel.adorn.util.containsOldUuid
import juuxel.adorn.util.getOldUuid
import juuxel.adorn.util.getText
import juuxel.adorn.util.putText
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import java.util.UUID

class TradingStationBlockEntity : BlockEntity(AdornBlockEntities.TRADING_STATION), BlockEntityClientSerializable, ExtendedScreenHandlerFactory, TradingStation {
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
        TradingStationController(syncId, playerInv, ScreenHandlerContext.create(world, pos), isOwner(player))

    override fun getDisplayName() = TranslatableText(cachedState.block.translationKey)

    override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
        buf.writeBlockPos(pos)
    }

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

    override fun toClientTag(tag: CompoundTag) = tag.apply {
        putText(NBT_TRADING_OWNER_NAME, ownerName)
        put(NBT_TRADE, trade.toTag(CompoundTag()))
    }

    override fun fromClientTag(tag: CompoundTag) {
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
