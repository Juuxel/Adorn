package juuxel.adorn.platform.forge.block.entity

import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.util.getText
import juuxel.adorn.util.putText
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.math.BlockPos

class TradingStationBlockEntityForge(pos: BlockPos, state: BlockState) : TradingStationBlockEntity(pos, state)
