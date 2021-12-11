package juuxel.adorn.block.entity

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class TradingStationBlockEntityFabric(pos: BlockPos, state: BlockState) : TradingStationBlockEntity(pos, state), ExtendedScreenHandlerFactory
