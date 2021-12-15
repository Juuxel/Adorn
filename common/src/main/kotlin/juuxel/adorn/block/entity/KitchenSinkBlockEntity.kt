package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

open class KitchenSinkBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(AdornBlockEntities.KITCHEN_SINK, pos, state)
