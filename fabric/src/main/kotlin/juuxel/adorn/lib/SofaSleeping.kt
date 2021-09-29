package juuxel.adorn.lib

import juuxel.adorn.block.SofaBlock
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.minecraft.util.ActionResult

object SofaSleeping {
    fun init() {
        EntitySleepEvents.ALLOW_BED.register { _, _, state, _ ->
            if (state.block is SofaBlock) ActionResult.SUCCESS
            else ActionResult.PASS
        }

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register { player, sleepingPos ->
            player.world.getBlockState(sleepingPos).block !is SofaBlock
        }

        EntitySleepEvents.ALLOW_SLEEP_TIME.register { player, sleepingPos, _ ->
            if (player.world.isDay && player.world.getBlockState(sleepingPos).block is SofaBlock) ActionResult.SUCCESS
            else ActionResult.PASS
        }

        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register { entity, sleepingPos, sleepingDirection ->
            if (entity.world.getBlockState(sleepingPos).block is SofaBlock)
                SofaBlock.getSleepingDirection(entity.world, sleepingPos, ignoreNeighbors = true)?.opposite
            else sleepingDirection
        }

        EntitySleepEvents.ALLOW_RESETTING_TIME.register { player ->
            val pos = player.sleepingPosition.orElse(null) ?: return@register true

            if (player.world.getBlockState(pos).block is SofaBlock) {
                if (player.world.isDay) false
                else player.world.gameRules.getBoolean(AdornGameRules.SKIP_NIGHT_ON_SOFAS)
            } else {
                true // go on
            }
        }
    }
}
