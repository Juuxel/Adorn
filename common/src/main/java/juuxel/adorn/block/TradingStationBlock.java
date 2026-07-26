package juuxel.adorn.block;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.criterion.AdornCriteria;
import juuxel.adorn.lib.AdornStats;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public final class TradingStationBlock extends VisibleBlockWithEntity implements BlockWithDescription {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape OUTLINE_SHAPE = Shapes.or(
        box(0.0, 11.0, 0.0, 16.0, 16.0, 16.0),
        createLegShape()
    );
    private static final VoxelShape COLLISION_SHAPE = Shapes.or(
        box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
        createLegShape()
    );

    public TradingStationBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (placer instanceof Player player && world.getBlockEntity(pos) instanceof TradingStationBlockEntity tradingStation) {
            tradingStation.setOwnerIfMissing(player);
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide()) return InteractionResult.SUCCESS_SERVER;

        if (world.getBlockEntity(pos) instanceof TradingStationBlockEntity be) {
            be.setOwnerIfMissing(player);

            if (!be.isOwner(player)) {
                var trade = be.getTrade();
                var validPayment = ItemStack.isSameItemSameComponents(stack, trade.getPrice()) &&
                    stack.getCount() >= trade.getPrice().getCount();
                var canInsertPayment = be.getStorage().canInsert(trade.getPrice());

                if (trade.isEmpty()) {
                    player.displayClientMessage(Component.translatable("block.adorn.trading_station.empty_trade"), true);
                } else if (!be.isStorageStocked()) {
                    player.displayClientMessage(Component.translatable("block.adorn.trading_station.storage_not_stocked"), true);
                } else if (!canInsertPayment) {
                    player.displayClientMessage(Component.translatable("block.adorn.trading_station.storage_full"), true);
                } else if (validPayment) {
                    stack.shrink(trade.getPrice().getCount());
                    var soldItem = trade.getSelling().copy();
                    player.addItem(soldItem);
                    be.getStorage().tryExtract(trade.getSelling());
                    be.getStorage().tryInsert(trade.getPrice());
                    player.awardStat(AdornStats.INTERACT_WITH_TRADING_STATION);

                    if (player instanceof ServerPlayer serverPlayer) {
                        AdornCriteria.BOUGHT_FROM_TRADING_STATION.get().trigger(serverPlayer, soldItem);
                    }
                }
            } else {
                player.openMenu(be);
                player.awardStat(AdornStats.INTERACT_WITH_TRADING_STATION);
            }
        }

        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, world, pos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.TRADING_STATION.get().create(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException();
    }

    private static VoxelShape createLegShape() {
        return Shapes.or(
            box(1.0, 0.0, 1.0, 4.0, 14.0, 4.0),
            box(12.0, 0.0, 1.0, 15.0, 14.0, 4.0),
            box(1.0, 0.0, 12.0, 4.0, 14.0, 15.0),
            box(12.0, 0.0, 12.0, 15.0, 14.0, 15.0)
        );
    }
}
