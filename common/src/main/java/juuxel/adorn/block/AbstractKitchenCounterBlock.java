package juuxel.adorn.block;

import juuxel.adorn.util.Shapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.Map;

public abstract class AbstractKitchenCounterBlock extends Block {
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BlockSoundGroup SOUND_GROUP = new BlockSoundGroup(
        1.0F, 1.0F,
        SoundEvents.BLOCK_WOOD_BREAK,
        SoundEvents.BLOCK_STONE_STEP,
        SoundEvents.BLOCK_WOOD_PLACE,
        SoundEvents.BLOCK_WOOD_HIT,
        SoundEvents.BLOCK_STONE_FALL
    );
    protected static final Map<Direction, VoxelShape> SHAPES = Shapes.mergeIntoShapeMap(
        Shapes.buildShapeRotationsFromNorth(
            0, 0, 2,
            16, 12, 16
        ),
        createCuboidShape(
            0.0, 12.0, 0.0,
            16.0, 16.0, 16.0
        )
    );

    public AbstractKitchenCounterBlock(Settings settings) {
        super(settings.sounds(SOUND_GROUP));
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
}
