package juuxel.adorn

import juuxel.adorn.block.AdornBlockEntities
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos

object CommonEventHandlers {
    // TODO: This currently targets block attacking. What about explosions and mining machines?
    fun shouldCancelBlockBreak(player: PlayerEntity, pos: BlockPos): Boolean {
        if (player.isSpectator || player.isCreativeLevelTwoOp) return false

        val tradingStation = player.world.getBlockEntity(pos, AdornBlockEntities.TRADING_STATION).orElse(null) ?: return false
        return !tradingStation.isOwner(player)
    }
}
