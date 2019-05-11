package juuxel.adorn.block

import com.google.common.base.Predicates
import io.github.juuxel.polyester.block.PolyesterBlock
import juuxel.adorn.lib.ModEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.SpawnType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BoundingBox
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

abstract class SeatBlock(settings: Settings) : Block(settings), PolyesterBlock {
    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult
    ): Boolean {
        val entities = world.getEntities(ModEntities.SITTING_VEHICLE, BoundingBox(pos), Predicates.alwaysTrue())

        return if (!world.isClient && entities.isEmpty()) {
            val entity = ModEntities.SITTING_VEHICLE.spawn(world, null, null, player, pos, SpawnType.TRIGGERED, false, false)
            entity?.setPos(pos)
            player.startRiding(entity, true)
            true
        } else false
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}
