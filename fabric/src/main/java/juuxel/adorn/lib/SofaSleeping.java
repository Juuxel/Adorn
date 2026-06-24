package juuxel.adorn.lib;

import juuxel.adorn.block.SofaBlock;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.block.BedBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public final class SofaSleeping {
    public static void init() {
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) ->
            state.getBlock() instanceof SofaBlock ? ActionResult.SUCCESS : ActionResult.PASS);

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, sleepingPos) ->
            !(player.getEntityWorld().getBlockState(sleepingPos).getBlock() instanceof SofaBlock));

        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register((entity, sleepingPos, sleepingDirection) -> {
            if (entity.getEntityWorld().getBlockState(sleepingPos).getBlock() instanceof SofaBlock) {
                var direction = SofaBlock.getSleepingDirection(entity.getEntityWorld(), sleepingPos, true);
                return direction != null ? direction.getOpposite() : null;
            }

            return sleepingDirection;
        });

        EntitySleepEvents.ALLOW_RESETTING_TIME.register(player -> {
            var pos = player.getSleepingPosition().orElse(null);
            if (pos == null) return true;

            if (player.getEntityWorld().getBlockState(pos).getBlock() instanceof SofaBlock) {
                if (player.getEntityWorld().isDay()) {
                    return false;
                } else {
                    return player.getEntityWorld() instanceof ServerWorld world && world.getGameRules().getValue(AdornGameRules.SKIP_NIGHT_ON_SOFAS.get());
                }
            } else {
                return true; // go on
            }
        });

        EntitySleepEvents.SET_BED_OCCUPATION_STATE.register((entity, pos, state, occupied) -> {
            var world = entity.getEntityWorld();

            if (state.getBlock() instanceof SofaBlock) {
                world.setBlockState(pos, state.with(SofaBlock.OCCUPIED, occupied));
                var neighborPos = pos.offset(SofaBlock.getSleepingDirection(world, pos, true));
                world.setBlockState(neighborPos, world.getBlockState(neighborPos).with(SofaBlock.OCCUPIED, occupied));
                return true;
            } else {
                return false; // go on
            }
        });

        EntitySleepEvents.MODIFY_WAKE_UP_POSITION.register((entity, sleepingPos, state, wakeUpPos) -> {
            if (state.getBlock() instanceof SofaBlock) {
                var direction = SofaBlock.getSleepingDirection(entity.getEntityWorld(), sleepingPos, true);
                return BedBlock.findWakeUpPosition(entity.getType(), entity.getEntityWorld(), sleepingPos, direction, entity.getYaw()).orElse(null);
            } else {
                return wakeUpPos;
            }
        });
    }
}
