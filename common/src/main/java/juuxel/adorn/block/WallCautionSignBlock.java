package juuxel.adorn.block;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.util.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SideShapeType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;

public final class WallCautionSignBlock extends Block {
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> SHAPES = Shapes.buildShapeRotationsFromNorth(1, 1, 15, 15, 15, 16);
    private final Registered<? extends Block> standing;

    public WallCautionSignBlock(Settings settings, Registered<? extends Block> standing) {
        super(settings);
        this.standing = standing;
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    public static Settings createBlockSettings(Registered<? extends Block> standing) {
         return Settings.create()
             .dropsLike(standing.get())
             .mapColor(MapColor.YELLOW)
             .sounds(BlockSoundGroup.STONE)
             .strength(1f)
             .noCollision();
    }

    @Override
    public String getTranslationKey() {
        return standing.get().getTranslationKey();
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction facing = state.get(FACING);
        return world.getBlockState(pos.offset(facing.getOpposite()))
            .isSideSolid(world, pos, facing, SideShapeType.CENTER);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction[] placementDirections = ctx.getPlacementDirections();

        for (Direction direction : placementDirections) {
            if (!direction.getAxis().isHorizontal()) continue;

            var state = getDefaultState().with(FACING, direction.getOpposite());
            if (state.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                return state.with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
            }
        }

        return null;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.get(FACING).getOpposite() && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return state;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, WATERLOGGED);
    }
}
