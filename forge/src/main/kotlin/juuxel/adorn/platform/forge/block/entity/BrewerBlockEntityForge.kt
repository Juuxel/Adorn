package juuxel.adorn.platform.forge.block.entity

import juuxel.adorn.block.entity.BrewerBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.wrapper.SidedInvWrapper

class BrewerBlockEntityForge(pos: BlockPos, state: BlockState) : BrewerBlockEntity(pos, state) {
    private var itemHandlers = SidedInvWrapper.create(this, *Direction.values().sortedArrayWith(compareBy { it.id }))

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (!removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return if (side != null) itemHandlers[side.id].cast() else LazyOptional.empty()
        }

        return super.getCapability(cap, side)
    }

    override fun invalidateCaps() {
        super.invalidateCaps()

        for (itemHandler in itemHandlers) {
            itemHandler.invalidate()
        }
    }

    override fun reviveCaps() {
        super.reviveCaps()
        itemHandlers = SidedInvWrapper.create(this, *Direction.values())
    }
}
