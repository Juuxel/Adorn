package juuxel.adorn.lib;

import juuxel.adorn.block.SofaBlock;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;

public final class SofaSleeping {
    public static void init() {
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) ->
            state.getBlock() instanceof SofaBlock ? InteractionResult.SUCCESS : InteractionResult.PASS);

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, sleepingPos) ->
            !(player.level().getBlockState(sleepingPos).getBlock() instanceof SofaBlock));

        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register((entity, sleepingPos, sleepingDirection) -> {
            if (entity.level().getBlockState(sleepingPos).getBlock() instanceof SofaBlock) {
                var direction = SofaBlock.getSleepingDirection(entity.level(), sleepingPos, true);
                return direction != null ? direction.getOpposite() : null;
            }

            return sleepingDirection;
        });

        EntitySleepEvents.ALLOW_RESETTING_TIME.register(player -> {
            var pos = player.getSleepingPos().orElse(null);
            if (pos == null) return true;

            if (player.level().getBlockState(pos).getBlock() instanceof SofaBlock) {
                if (player.level().isBrightOutside()) {
                    return false;
                } else {
                    return player.level() instanceof ServerLevel world && world.getGameRules().get(AdornGameRules.SKIP_NIGHT_ON_SOFAS.get());
                }
            } else {
                return true; // go on
            }
        });

        EntitySleepEvents.SET_BED_OCCUPATION_STATE.register((entity, pos, state, occupied) -> {
            var world = entity.level();

            if (state.getBlock() instanceof SofaBlock) {
                world.setBlockAndUpdate(pos, state.setValue(SofaBlock.OCCUPIED, occupied));
                var neighborPos = pos.relative(SofaBlock.getSleepingDirection(world, pos, true));
                world.setBlockAndUpdate(neighborPos, world.getBlockState(neighborPos).setValue(SofaBlock.OCCUPIED, occupied));
                return true;
            } else {
                return false; // go on
            }
        });

        EntitySleepEvents.MODIFY_WAKE_UP_POSITION.register((entity, sleepingPos, state, wakeUpPos) -> {
            if (state.getBlock() instanceof SofaBlock) {
                var direction = SofaBlock.getSleepingDirection(entity.level(), sleepingPos, true);
                return BedBlock.findStandUpPosition(entity.getType(), entity.level(), sleepingPos, direction, entity.getYRot()).orElse(null);
            } else {
                return wakeUpPos;
            }
        });
    }
}
