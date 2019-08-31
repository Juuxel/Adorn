package juuxel.adorn.block

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface Colorable {
    /**
     * Applies the color ([red], [green] and [blue]) to the block at the [pos].
     *
     * @param world the world
     */
    fun applyColor(world: World, pos: BlockPos, state: BlockState, red: Int, green: Int, blue: Int)
}
