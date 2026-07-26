package juuxel.adorn.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implement on blocks to give them a sneak-click action.
 */
public interface SneakClickHandler {
    InteractionResult onSneakClick(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);
}
