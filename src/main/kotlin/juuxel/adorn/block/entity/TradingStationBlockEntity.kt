package juuxel.adorn.block.entity

import juuxel.adorn.block.TradingStationBlock
import juuxel.adorn.gui.controller.DrawerController
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
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import java.util.UUID

class TradingStationBlockEntity : BlockEntity(TradingStationBlock.BLOCK_ENTITY_TYPE), BlockEntityClientSerializable, NameableContainerProvider, TradingStation {
    var owner: UUID? = null
    var ownerName: Component = TextComponent("???")
    override val trade: Trade = Trade(ItemStack.EMPTY, ItemStack.EMPTY)
    override val storage: InventoryComponent = InventoryComponent(12)

    init {
        trade.addListener {
            markDirty()
        }
    }

    fun setOwner(player: PlayerEntity) {
        owner = player.gameProfile.id
        ownerName = TextComponent(player.gameProfile.name)
        markDirty()
    }

    fun isStorageStocked(): Boolean =
        storage.getInvAmountOf(trade.selling.item) >= trade.selling.amount

    fun isOwner(player: PlayerEntity) = player.gameProfile.id == owner

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity) =
        if (isOwner(player))
            TradingStationController(syncId, playerInv, BlockContext.create(world, pos))
        else null

    override fun getDisplayName() = TranslatableComponent(cachedState.block.translationKey)

    // NBT

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        owner = tag.getUuid(NBT_TRADING_OWNER)
        ownerName = tag.getTextComponent(NBT_TRADING_OWNER_NAME) ?: TextComponent("??")
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
