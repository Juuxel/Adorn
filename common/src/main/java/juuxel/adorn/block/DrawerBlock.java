package juuxel.adorn.block;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.block.entity.DrawerBlockEntity;
import juuxel.adorn.block.entity.SimpleContainerBlockEntity;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.lib.AdornStats;
import juuxel.adorn.platform.PlatformBridges;
import juuxel.adorn.util.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.menu.Menu;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
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

public final class DrawerBlock extends VisibleBlockWithEntity implements BlockWithDescription {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.mergeShapeMaps(
        Shapes.buildShapeRotationsFromNorth(0, 0, 3, 16, 16, 16),
        // Top drawer
        Shapes.buildShapeRotationsFromNorth(1, 9, 1, 15, 15, 3),
        // Bottom drawer
        Shapes.buildShapeRotationsFromNorth(1, 1, 1, 15, 7, 3)
    );
    private static final String DESCRIPTION_KEY = "block.adorn.drawer.description";

    public DrawerBlock(BlockVariant variant) {
        super(variant.createSettings().nonOpaque());
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        if (world.getBlockEntity(pos) instanceof DrawerBlockEntity drawer) {
            PlatformBridges.get().getMenus().open(player, drawer, pos);;
            player.incrementStat(AdornStats.OPEN_DRAWER);
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
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
        return AdornBlockEntities.DRAWER.get().instantiate(pos, state);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockEntity(pos) instanceof SimpleContainerBlockEntity container) {
            container.onScheduledTick();
        }
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        throw new UnsupportedOperationException();
    }
}
