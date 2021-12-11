package juuxel.adorn.block.entity

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class KitchenCupboardBlockEntityFabric(pos: BlockPos, state: BlockState) : KitchenCupboardBlockEntity(pos, state), ExtendedScreenHandlerFactory
