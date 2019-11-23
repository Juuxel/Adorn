package juuxel.adorn.client

import net.minecraft.block.BlockState
import net.minecraft.client.color.block.BlockColorProvider
import net.minecraft.client.color.world.BiomeColors
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

object SinkColorProvider : BlockColorProvider {
    override fun getColor(state: BlockState, view: BlockRenderView?, pos: BlockPos?, tintIndex: Int) =
        if (tintIndex == 1 && view != null && pos != null) BiomeColors.getWaterColor(view, pos)
        else -1
}
