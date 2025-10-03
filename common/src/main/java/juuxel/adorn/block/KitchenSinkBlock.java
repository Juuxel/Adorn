package juuxel.adorn.block;

import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.util.Shapes;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public final class KitchenSinkBlock extends KitchenCounterBlock implements BlockEntityProvider, BlockWithDescription {
    private static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);
    private static final String DESCRIPTION_KEY = "block.adorn.kitchen_sink.description";

    static {
        var sinkShapes = Shapes.buildShapeRotationsFromNorth(
            2, 7, 3,
            14, 16, 14
        );
        AbstractKitchenCounterBlock.SHAPES.forEach((direction, shape) -> {
            SHAPES.put(direction, VoxelShapes.combineAndSimplify(shape, sinkShapes.get(direction), BooleanBiFunction.ONLY_FIRST));
        });
    }

    public KitchenSinkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var entity = world.getBlockEntity(pos, AdornBlockEntities.KITCHEN_SINK.get()).orElse(null);
        if (entity == null) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
        boolean successful;

        if (stack.isOf(Items.SPONGE)) {
            successful = entity.clearFluidsWithSponge();

            if (successful) {
                stack.decrement(1);
                var wetStack = new ItemStack(Items.WET_SPONGE);

                if (stack.isEmpty()) {
                    player.setStackInHand(hand, wetStack);
                } else {
                    player.getInventory().offerOrDrop(wetStack);
                }
            }
        } else {
            successful = entity.interactWithItem(stack, player, hand);
        }

        return successful ? ActionResult.SUCCESS : ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        return world.getBlockEntity(pos) instanceof KitchenSinkBlockEntity sink ? sink.calculateComparatorOutput() : 0;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return AdornBlockEntities.KITCHEN_SINK.get().instantiate(pos, state);
    }
}
