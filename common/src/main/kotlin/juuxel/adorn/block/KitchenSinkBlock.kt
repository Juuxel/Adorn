package juuxel.adorn.block

import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.util.buildShapeRotationsFromNorth
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class KitchenSinkBlock(variant: BlockVariant) : KitchenCounterBlock(variant), BlockEntityProvider, BlockWithDescription {
    override val descriptionKey = "block.adorn.kitchen_sink.description"

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        val entity = world.getBlockEntity(pos, AdornBlockEntities.KITCHEN_SINK).orElse(null) ?: return ActionResult.PASS
        val stack = player.getStackInHand(hand)
        val successful = if (stack.isOf(Items.SPONGE)) {
            val result = entity.clearFluidsWithSponge()

            if (result) {
                stack.decrement(1)
                val wetStack = ItemStack(Items.WET_SPONGE)

                if (stack.isEmpty) {
                    player.setStackInHand(hand, wetStack)
                } else {
                    player.inventory.offerOrDrop(wetStack)
                }
            }

            result
        } else {
            entity.interactWithItem(stack, player, hand)
        }

        return if (successful) ActionResult.success(world.isClient) else ActionResult.PASS
    }

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext) =
        SHAPES[state[FACING]]

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int =
        world.getBlockEntity(pos, AdornBlockEntities.KITCHEN_SINK).orElse(null)
            ?.calculateComparatorOutput() ?: 0

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.KITCHEN_SINK.instantiate(pos, state)

    companion object {
        private val SHAPES: Map<Direction, VoxelShape>

        init {
            val sinkShapes = buildShapeRotationsFromNorth(
                2, 7, 3,
                14, 16, 14
            )
            SHAPES = AbstractKitchenCounterBlock.SHAPES.mapValues { (direction, shape) ->
                VoxelShapes.combineAndSimplify(
                    shape,
                    sinkShapes[direction] ?: error(
                        "Error building kitchen sink shapes: couldn't find sink shape for direction '$direction'"
                    ),
                    BooleanBiFunction.ONLY_FIRST
                )
            }
        }
    }
}
