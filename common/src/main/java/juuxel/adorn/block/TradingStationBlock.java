package juuxel.adorn.block;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.criterion.AdornCriteria;
import juuxel.adorn.lib.AdornStats;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class TradingStationBlock extends VisibleBlockWithEntity implements BlockWithDescription {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(
        createCuboidShape(0.0, 11.0, 0.0, 16.0, 16.0, 16.0),
        createLegShape()
    );
    private static final VoxelShape COLLISION_SHAPE = VoxelShapes.union(
        createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
        createLegShape()
    );

    public TradingStationBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer instanceof PlayerEntity player && world.getBlockEntity(pos) instanceof TradingStationBlockEntity tradingStation) {
            tradingStation.setOwnerIfMissing(player);
        }
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS_SERVER;

        if (world.getBlockEntity(pos) instanceof TradingStationBlockEntity be) {
            be.setOwnerIfMissing(player);

            if (!be.isOwner(player)) {
                var trade = be.getTrade();
                var validPayment = ItemStack.areItemsAndComponentsEqual(stack, trade.getPrice()) &&
                    stack.getCount() >= trade.getPrice().getCount();
                var canInsertPayment = be.getStorage().canInsert(trade.getPrice());

                if (trade.isEmpty()) {
                    player.sendMessage(Text.translatable("block.adorn.trading_station.empty_trade"), true);
                } else if (!be.isStorageStocked()) {
                    player.sendMessage(Text.translatable("block.adorn.trading_station.storage_not_stocked"), true);
                } else if (!canInsertPayment) {
                    player.sendMessage(Text.translatable("block.adorn.trading_station.storage_full"), true);
                } else if (validPayment) {
                    stack.decrement(trade.getPrice().getCount());
                    var soldItem = trade.getSelling().copy();
                    player.giveItemStack(soldItem);
                    be.getStorage().tryExtract(trade.getSelling());
                    be.getStorage().tryInsert(trade.getPrice());
                    player.incrementStat(AdornStats.INTERACT_WITH_TRADING_STATION);

                    if (player instanceof ServerPlayerEntity serverPlayer) {
                        AdornCriteria.BOUGHT_FROM_TRADING_STATION.get().trigger(serverPlayer, soldItem);
                    }
                }
            } else {
                player.openMenu(be);
                player.incrementStat(AdornStats.INTERACT_WITH_TRADING_STATION);
            }
        }

        return ActionResult.SUCCESS_SERVER;
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        ItemScatterer.onStateReplaced(state, world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.TRADING_STATION.get().instantiate(pos, state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        throw new UnsupportedOperationException();
    }

    private static VoxelShape createLegShape() {
        return VoxelShapes.union(
            createCuboidShape(1.0, 0.0, 1.0, 4.0, 14.0, 4.0),
            createCuboidShape(12.0, 0.0, 1.0, 15.0, 14.0, 4.0),
            createCuboidShape(1.0, 0.0, 12.0, 4.0, 14.0, 15.0),
            createCuboidShape(12.0, 0.0, 12.0, 15.0, 14.0, 15.0)
        );
    }
}
