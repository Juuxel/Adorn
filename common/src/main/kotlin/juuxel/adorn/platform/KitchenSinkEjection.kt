package juuxel.adorn.platform

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface KitchenSinkEjection {
    fun eject(world: World, pos: BlockPos)
}
