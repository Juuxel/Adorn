package juuxel.adorn.platform

import juuxel.adorn.entity.SeatEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos

interface EntityBridge {
    fun createSeatType(): EntityType<SeatEntity>
    fun PlayerEntity.trySleep(pos: BlockPos, onSuccess: () -> Unit)
}
