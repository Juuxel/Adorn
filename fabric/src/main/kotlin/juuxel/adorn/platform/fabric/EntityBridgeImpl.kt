package juuxel.adorn.platform.fabric

import juuxel.adorn.entity.SeatEntity
import juuxel.adorn.platform.EntityBridge
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos

object EntityBridgeImpl : EntityBridge {
    override fun createSeatType(): EntityType<SeatEntity> =
        FabricEntityTypeBuilder.create<SeatEntity>(SpawnGroup.MISC) { type, world -> SeatEntity(type, world) }
            .dimensions(EntityDimensions.fixed(0f, 0f))
            .disableSaving()
            .build()

    override fun PlayerEntity.trySleep(pos: BlockPos, onSuccess: () -> Unit) {
        trySleep(pos).ifRight { onSuccess() }.ifLeft {
            it.toText()?.let { message ->
                sendMessage(message, true)
            }
        }
    }
}
