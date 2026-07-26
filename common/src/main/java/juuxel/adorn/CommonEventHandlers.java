package juuxel.adorn;

import juuxel.adorn.block.CarpetedBlock;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

public final class CommonEventHandlers {
    public static InteractionResult handleCarpets(Player player, Level world, InteractionHand hand, BlockHitResult hit) {
        var stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof WoolCarpetBlock carpet) {
                var pos = hit.getBlockPos().relative(hit.getDirection());
                var state = world.getBlockState(pos);

                if (state.getBlock() instanceof CarpetedBlock carpeted && carpeted.canStateBeCarpeted(state)) {
                    world.setBlockAndUpdate(pos, state.setValue(CarpetedBlock.CARPET, CarpetedBlock.CARPET.wrapOrNone(carpet.getColor())));
                    var soundGroup = carpet.defaultBlockState().getSoundType();
                    world.playSound(
                        player, pos, soundGroup.getPlaceSound(), SoundSource.BLOCKS,
                        (soundGroup.volume + 1f) * 0.5f, soundGroup.pitch * 0.8f
                    );

                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    player.swing(hand);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }
}
