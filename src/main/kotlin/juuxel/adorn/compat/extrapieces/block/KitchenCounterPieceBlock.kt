package juuxel.adorn.compat.extrapieces.block

import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.block.KitchenCounterBlock
import juuxel.adorn.compat.extrapieces.AdornPieces
import juuxel.adorn.compat.extrapieces.toVariant
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.Random

class KitchenCounterPieceBlock(private val set: PieceSet) : KitchenCounterBlock(set.toVariant()), AdornPieceBlock {
    override fun getSet() = set

    override fun getType() = AdornPieces.KITCHEN_COUNTER

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
}
