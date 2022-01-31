package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.menu.TradingStationMenu
import juuxel.adorn.trading.Trade
import juuxel.adorn.util.InventoryComponent
import juuxel.adorn.util.containsOldUuid
import juuxel.adorn.util.getOldUuid
import juuxel.adorn.util.getText
import juuxel.adorn.util.menuContextOf
import juuxel.adorn.util.putText
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos
import java.util.UUID

class TradingStationBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(AdornBlockEntities.TRADING_STATION, pos, state), NamedMenuFactory, TradingStation {
    var owner: UUID? = null
    override var ownerName: Text = LiteralText("???")
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

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity): Menu =
        TradingStationMenu(syncId, playerInv, menuContextOf(this))

    override fun getDisplayName() = TranslatableText(cachedState.block.translationKey)

    // NBT

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)

        if (nbt.containsUuid(NBT_TRADING_OWNER)) {
            owner = nbt.getUuid(NBT_TRADING_OWNER)
        } else if (nbt.containsOldUuid(NBT_TRADING_OWNER)) {
            owner = nbt.getOldUuid(NBT_TRADING_OWNER)
        }

        ownerName = nbt.getText(NBT_TRADING_OWNER_NAME) ?: LiteralText("??")
        trade.readNbt(nbt.getCompound(NBT_TRADE))
        storage.readNbt(nbt.getCompound(NBT_STORAGE))
    }

    override fun writeNbt(nbt: NbtCompound) = super.writeNbt(nbt).apply {
        if (owner != null) {
            nbt.putUuid(NBT_TRADING_OWNER, owner)
        }

        nbt.putText(NBT_TRADING_OWNER_NAME, ownerName)

        nbt.put(NBT_TRADE, trade.writeNbt(NbtCompound()))
        nbt.put(NBT_STORAGE, storage.writeNbt(NbtCompound()))
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> =
        BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

    companion object {
        const val NBT_TRADING_OWNER = "TradingOwner"
        const val NBT_TRADING_OWNER_NAME = "TradingOwnerName"
        const val NBT_TRADE = "Trade"
        const val NBT_STORAGE = "Storage"
    }
}
