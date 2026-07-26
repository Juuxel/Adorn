package juuxel.adorn.block;

import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.util.ShapeRotation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public final class KitchenSinkBlock extends KitchenCounterBlock implements EntityBlock, BlockWithDescription {
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);
    private static final String DESCRIPTION_KEY = "block.adorn.kitchen_sink.description";

    static {
        var sinkShapes = ShapeRotation.buildShapeRotationsFromNorth(
            2, 7, 3,
            14, 16, 14
        );
        AbstractKitchenCounterBlock.SHAPES.forEach((direction, shape) -> {
            SHAPES.put(direction, Shapes.join(shape, sinkShapes.get(direction), BooleanOp.ONLY_FIRST));
        });
    }

    public KitchenSinkBlock(Properties settings) {
        super(settings);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var entity = world.getBlockEntity(pos, AdornBlockEntities.KITCHEN_SINK.get()).orElse(null);
        if (entity == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
        boolean successful;

        if (stack.is(Items.SPONGE)) {
            successful = entity.clearFluidsWithSponge();

            if (successful) {
                stack.shrink(1);
                var wetStack = new ItemStack(Items.WET_SPONGE);

                if (stack.isEmpty()) {
                    player.setItemInHand(hand, wetStack);
                } else {
                    player.getInventory().placeItemBackInInventory(wetStack);
                }
            }
        } else {
            successful = entity.interactWithItem(stack, player, hand);
        }

        return successful ? InteractionResult.SUCCESS : InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return world.getBlockEntity(pos) instanceof KitchenSinkBlockEntity sink ? sink.calculateComparatorOutput() : 0;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.KITCHEN_SINK.get().create(pos, state);
    }
}
