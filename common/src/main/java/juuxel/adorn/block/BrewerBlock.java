package juuxel.adorn.block;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.util.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class BrewerBlock extends VisibleBlockWithEntity implements BlockWithDescription {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty HAS_MUG = BooleanProperty.of("has_mug");
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    private static final Map<Direction, VoxelShape> SHAPES = Shapes.mergeShapeMaps(
        Shapes.buildShapeRotationsFromNorth(4, 0, 2, 12, 2, 12),
        Shapes.buildShapeRotationsFromNorth(4, 2, 8, 12, 8, 12),
        Shapes.buildShapeRotationsFromNorth(4, 8, 2, 12, 14, 12)
    );
    private static final double RANDOM_CLOUD_OFFSET = 0.0625;
    private static final double FACING_CLOUD_OFFSET = 0.2;

    public BrewerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(HAS_MUG, false).with(ACTIVE, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof BrewerBlockEntity brewer) {
            player.openMenu(brewer);
            player.incrementStat(AdornStats.OPEN_BREWER);
        }

        return ActionResult.CONSUME;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.BREWER.get().instantiate(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return !world.isClient ? validateTicker(type, AdornBlockEntities.BREWER.get(), BrewerBlockEntity::tick) : null;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_MUG, ACTIVE);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof BrewerBlockEntity brewer ? brewer.calculateComparatorOutput() : 0;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(ACTIVE) && random.nextInt(3) == 0) {
            var facing = state.get(FACING);
            double x = pos.getX() + 0.5 + random.nextDouble() * RANDOM_CLOUD_OFFSET + facing.getOffsetX() * FACING_CLOUD_OFFSET;
            double y = pos.getY() + 0.37 + random.nextDouble() * RANDOM_CLOUD_OFFSET;
            double z = pos.getZ() + 0.5 + random.nextDouble() * RANDOM_CLOUD_OFFSET + facing.getOffsetZ() * FACING_CLOUD_OFFSET;
            world.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        throw new UnsupportedOperationException();
    }
}
