package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.util.buildShapeRotations
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.Waterloggable
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsage
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
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
import net.minecraft.world.event.GameEvent

class KitchenSinkBlock(variant: BlockVariant) : KitchenCounterBlock(variant), Waterloggable {
    init {
        defaultState = defaultState.with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(WATERLOGGED)
    }

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, context: ShapeContext) =
        SHAPES[state[FACING]]

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        if (state[WATERLOGGED]) 15
        else 0

    // This is needed so that Towelette's default impl of getFluidState doesn't return water here.
    override fun getFluidState(state: BlockState?): FluidState =
        Fluids.EMPTY.defaultState

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult?
    ): ActionResult {
        val handStack = player.getStackInHand(hand);

        if (handStack.item.equals(Items.POTION) && PotionUtil.getPotion(handStack).equals(Potions.WATER)) {
            player.setStackInHand(hand, ItemUsage.exchangeStack(handStack, player, ItemStack(Items.GLASS_BOTTLE)))
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f)
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos)
            return ActionResult.CONSUME
        }

        if (state[WATERLOGGED]) {
            if (handStack.item.equals(Items.GLASS_BOTTLE)) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(handStack, player, PotionUtil.setPotion(ItemStack(Items.POTION), Potions.WATER)))
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f)
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos)
                return ActionResult.CONSUME
            }
            if (handStack.item.equals(Items.BUCKET)) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(handStack, player, ItemStack(Items.WATER_BUCKET)))
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f)
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos)
                return ActionResult.CONSUME
            }
            if (handStack.item.equals(Items.WATER_BUCKET)) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(handStack, player, ItemStack(Items.BUCKET)))
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f)
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos)
                return ActionResult.CONSUME
            }
        }

        return ActionResult.PASS
    }

    companion object {
        private val WATERLOGGED = Properties.WATERLOGGED
        private val SHAPES: Map<Direction, VoxelShape>

        init {
            val sinkShapes = buildShapeRotations(
                2, 7, 2,
                13, 16, 14
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
