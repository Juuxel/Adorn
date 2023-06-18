package juuxel.adorn.block

import net.minecraft.block.AbstractCandleBlock
import net.minecraft.block.BlockState
import net.minecraft.block.LanternBlock
import net.minecraft.block.MapColor
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class CandlelitLanternBlock(settings: Settings) : LanternBlock(settings), BlockWithDescription {
    override val descriptionKey = "block.adorn.candlelit_lantern.description"

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        val px = 1 / 16.0
        val vec = Vec3d.ofCenter(pos, if (state[HANGING]) 6 * px else 5 * px)
        AbstractCandleBlock.spawnCandleParticles(world, vec, random)
    }

    companion object {
        fun createBlockSettings(): Settings =
            Settings.create()
                .mapColor(MapColor.IRON_GRAY)
                .solid()
                .requiresTool()
                .strength(3.5f)
                .sounds(BlockSoundGroup.LANTERN)
                .luminance { 12 }
                .nonOpaque()
    }
}
