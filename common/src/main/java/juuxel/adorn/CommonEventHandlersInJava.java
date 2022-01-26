package juuxel.adorn;

import juuxel.adorn.block.CarpetedBlock;
import net.minecraft.block.DyedCarpetBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

// TODO: Kotlin
public final class CommonEventHandlersInJava {
    public static ActionResult handleCarpets(PlayerEntity player, World world, Hand hand, BlockHitResult hit) {
        var stack = player.getStackInHand(hand);

        if (stack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof DyedCarpetBlock carpet) {
                var pos = hit.getBlockPos().offset(hit.getSide());
                var state = world.getBlockState(pos);

                if (state.getBlock() instanceof CarpetedBlock carpeted && carpeted.canStateBeCarpeted(state)) {
                    world.setBlockState(pos, state.with(CarpetedBlock.CARPET, CarpetedBlock.CARPET.wrapOrNone(carpet.getDyeColor())));
                    var soundGroup = carpet.getDefaultState().getSoundGroup();
                    world.playSound(
                        player, pos, soundGroup.getPlaceSound(), SoundCategory.BLOCKS,
                        (soundGroup.volume + 1f) / 2f, soundGroup.pitch * 0.8f
                    );

                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                    player.swingHand(hand);
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.PASS;
    }
}
