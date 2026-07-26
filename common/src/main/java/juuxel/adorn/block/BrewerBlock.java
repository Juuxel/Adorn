package juuxel.adorn.block;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.ShapeRotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.Containers;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class BrewerBlock extends VisibleBlockWithEntity implements BlockWithDescription {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_MUG = BooleanProperty.create("has_mug");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final Map<Direction, VoxelShape> SHAPES = ShapeRotation.mergeShapeMaps(
        ShapeRotation.buildShapeRotationsFromNorth(4, 0, 2, 12, 2, 12),
        ShapeRotation.buildShapeRotationsFromNorth(4, 2, 8, 12, 8, 12),
        ShapeRotation.buildShapeRotationsFromNorth(4, 8, 2, 12, 14, 12)
    );
    private static final double RANDOM_CLOUD_OFFSET = 0.0625;
    private static final double FACING_CLOUD_OFFSET = 0.2;

    public BrewerBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(HAS_MUG, false).setValue(ACTIVE, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (world.isClientSide()) return InteractionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof BrewerBlockEntity brewer) {
            player.openMenu(brewer);
            player.awardStat(AdornStats.OPEN_BREWER);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        Containers.updateNeighboursAfterDestroy(state, world, pos);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.BREWER.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level world, BlockState stateUnused, BlockEntityType<T> type) {
        return world instanceof ServerLevel serverWorld ? createTickerHelper(
            type,
            AdornBlockEntities.BREWER.get(),
            (worldUnused, pos, state, blockEntity) -> BrewerBlockEntity.tick(serverWorld, pos, state, blockEntity)
        ) : null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_MUG, ACTIVE);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return world.getBlockEntity(pos) instanceof BrewerBlockEntity brewer ? brewer.calculateComparatorOutput() : 0;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVE) && random.nextInt(3) == 0) {
            var facing = state.getValue(FACING);
            double x = pos.getX() + 0.5 + random.nextDouble() * RANDOM_CLOUD_OFFSET + facing.getStepX() * FACING_CLOUD_OFFSET;
            double y = pos.getY() + 0.37 + random.nextDouble() * RANDOM_CLOUD_OFFSET;
            double z = pos.getZ() + 0.5 + random.nextDouble() * RANDOM_CLOUD_OFFSET + facing.getStepZ() * FACING_CLOUD_OFFSET;
            world.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException();
    }
}
