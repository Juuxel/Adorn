@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.lib.AdornStats
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class TradingStationBlock : VisibleBlockWithEntity(Settings.copy(Blocks.CRAFTING_TABLE)), SneakClickHandler {
    init {
        defaultState = defaultState.with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!
            .with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack?) {
        if (entity is PlayerEntity) {
            val be = world.getBlockEntity(pos) as? TradingStationBlockEntity ?: return
            be.setOwner(entity)
        }
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult?
    ): ActionResult {
        val be = world.getBlockEntity(pos)
        if (be is TradingStationBlockEntity) {
            if (!world.isClient && be.owner == null) {
                be.setOwner(player)
            }

            if (!world.isClient && !be.isOwner(player)) {
                val handStack = player.getStackInHand(hand)
                val trade = be.trade
                val validPayment = handStack.isItemEqual(trade.price) &&
                    handStack.count >= trade.price.count &&
                    handStack.nbt == trade.price.nbt
                val canInsertPayment = be.storage.canInsert(trade.price)

                if (trade.isEmpty()) {
                    player.sendMessage(TranslatableText("block.adorn.trading_station.empty_trade"), true)
                } else if (!be.isStorageStocked()) {
                    player.sendMessage(TranslatableText("block.adorn.trading_station.storage_not_stocked"), true)
                } else if (!canInsertPayment) {
                    player.sendMessage(TranslatableText("block.adorn.trading_station.storage_full"), true)
                } else if (validPayment) {
                    handStack.decrement(trade.price.count)
                    player.giveItemStack(trade.selling.copy())
                    be.storage.tryExtract(trade.selling)
                    be.storage.tryInsert(trade.price)
                    player.incrementStat(AdornStats.INTERACT_WITH_TRADING_STATION)
                }
            } else {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos))

                if (!world.isClient) {
                    player.incrementStat(AdornStats.INTERACT_WITH_TRADING_STATION)
                }
            }
        }

        return ActionResult.SUCCESS
    }

    override fun onSneakClick(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult
    ): ActionResult {
        val be = world.getBlockEntity(pos) as? TradingStationBlockEntity ?: return ActionResult.PASS

        // Show customer GUI
        if (!be.isOwner(player)) {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos))

            if (!world.isClient) {
                player.incrementStat(AdornStats.INTERACT_WITH_TRADING_STATION)
            }

            return ActionResult.SUCCESS
        }

        return ActionResult.PASS
    }

    override fun onStateReplaced(state1: BlockState, world: World, pos: BlockPos, state2: BlockState, b: Boolean) {
        if (state1.block != state2.block) {
            val entity = world.getBlockEntity(pos)

            if (entity is TradingStationBlockEntity) {
                world.updateComparators(pos, this)
            }

            super.onStateReplaced(state1, world, pos, state2, b)
        }
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) =
        OUTLINE_SHAPE

    override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) =
        COLLISION_SHAPE

    override fun canPathfindThrough(state: BlockState, world: BlockView, pos: BlockPos, type: NavigationType) = false

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.TRADING_STATION.instantiate(pos, state)

    companion object {
        val WATERLOGGED = Properties.WATERLOGGED

        private val LEG_SHAPE = VoxelShapes.union(
            createCuboidShape(1.0, 0.0, 1.0, 4.0, 14.0, 4.0),
            createCuboidShape(12.0, 0.0, 1.0, 15.0, 14.0, 4.0),
            createCuboidShape(1.0, 0.0, 12.0, 4.0, 14.0, 15.0),
            createCuboidShape(12.0, 0.0, 12.0, 15.0, 14.0, 15.0)
        )

        private val OUTLINE_SHAPE = VoxelShapes.union(
            createCuboidShape(0.0, 11.0, 0.0, 16.0, 16.0, 16.0),
            LEG_SHAPE
        )

        private val COLLISION_SHAPE = VoxelShapes.union(
            createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0),
            LEG_SHAPE
        )
    }
}
