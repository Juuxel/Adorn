package juuxel.adorn.block;

import juuxel.adorn.util.ShapeRotation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import java.util.Map;

public abstract class AbstractKitchenCounterBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final SoundType SOUND_GROUP = new SoundType(
        1.0F, 1.0F,
        SoundEvents.WOOD_BREAK,
        SoundEvents.STONE_STEP,
        SoundEvents.WOOD_PLACE,
        SoundEvents.WOOD_HIT,
        SoundEvents.STONE_FALL
    );
    protected static final Map<Direction, VoxelShape> SHAPES = ShapeRotation.mergeIntoShapeMap(
        ShapeRotation.buildShapeRotationsFromNorth(
            0, 0, 2,
            16, 12, 16
        ),
        box(
            0.0, 12.0, 0.0,
            16.0, 16.0, 16.0
        )
    );

    public AbstractKitchenCounterBlock(Properties settings) {
        super(settings.sound(SOUND_GROUP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
}
