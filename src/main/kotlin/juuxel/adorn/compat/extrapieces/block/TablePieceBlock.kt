package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import java.util.Random
import juuxel.adorn.block.TableBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World

open class TablePieceBlock private constructor(private val set: PieceSet) : TableBlock(set.toVariant()), AdornPieceBlock {
    override fun isCarpetingEnabled() = false

    override fun getSet() = set

    override fun getType() = AdornPieces.TABLE

    // FIXME
    // override fun getRenderLayer() = base.renderLayer

    override fun emitsRedstonePower(state: BlockState?) =
        base.defaultState.emitsRedstonePower()

    override fun getWeakRedstonePower(state: BlockState?, world: BlockView?, pos: BlockPos?, side: Direction?) =
        base.defaultState.getWeakRedstonePower(world, pos, side)

    override fun getStrongRedstonePower(state: BlockState?, world: BlockView?, pos: BlockPos?, side: Direction?) =
        base.defaultState.getStrongRedstonePower(world, pos, side)

    @Environment(EnvType.CLIENT)
    override fun randomDisplayTick(state: BlockState?, world: World?, pos: BlockPos?, random: Random?) {
        super.randomDisplayTick(state, world, pos, random)
        base.randomDisplayTick(base.defaultState, world, pos, random)
    }

    class Carpeted(set: PieceSet) : TablePieceBlock(set) {
        override fun isCarpetingEnabled() = true
    }

    companion object {
        operator fun invoke(set: PieceSet): TablePieceBlock =
            if (AdornPieces.isCarpetingEnabled(set)) Carpeted(set) else TablePieceBlock(set)
    }
}
