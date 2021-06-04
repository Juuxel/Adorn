package juuxel.adorn.block.entity

import juuxel.adorn.util.getText
import juuxel.adorn.util.putText
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class TradingStationBlockEntityFabric(pos: BlockPos, state: BlockState) :
    TradingStationBlockEntity(pos, state),
    BlockEntityClientSerializable,
    ExtendedScreenHandlerFactory {
    override fun toClientTag(tag: NbtCompound) = tag.apply {
        putText(NBT_TRADING_OWNER_NAME, ownerName)
        put(NBT_TRADE, trade.writeNbt(NbtCompound()))
    }

    override fun fromClientTag(tag: NbtCompound) {
        trade.readNbt(tag.getCompound(NBT_TRADE))
        ownerName = tag.getText(NBT_TRADING_OWNER_NAME) ?: return
    }
}
