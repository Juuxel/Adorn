package juuxel.adorn.block;

import juuxel.adorn.block.entity.KitchenCupboardBlockEntity;
import juuxel.adorn.block.entity.SimpleContainerBlockEntity;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.menu.Menu;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class KitchenCupboardBlock extends AbstractKitchenCounterBlock implements BlockEntityProvider, BlockWithDescription {
    private static final String DESCRIPTION_KEY = "block.adorn.kitchen_cupboard.description";

    public KitchenCupboardBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof KitchenCupboardBlockEntity cupboard) {
            PlatformBridges.get().getMenus().open(player, cupboard, pos);
            player.incrementStat(AdornStats.OPEN_KITCHEN_CUPBOARD);
        }

        return ActionResult.CONSUME;
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        ItemScatterer.onStateReplaced(state, world, pos);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return Menu.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.KITCHEN_CUPBOARD.get().instantiate(pos, state);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockEntity(pos) instanceof SimpleContainerBlockEntity container) {
            container.onScheduledTick();
        }
    }
}
