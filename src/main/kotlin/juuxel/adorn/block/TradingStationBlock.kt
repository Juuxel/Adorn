package juuxel.adorn.block

import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.config.AdornConfigManager
import juuxel.adorn.gui.AdornGuis
import juuxel.adorn.gui.openFabricContainer
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.ShapeContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.TranslatableText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class TradingStationBlock : VisibleBlockWithEntity(Settings.copy(Blocks.CRAFTING_TABLE)),
    SneakClickHandler {
    override val blockEntityType = AdornBlockEntities.TRADING_STATION

    init {
        defaultState = defaultState.with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!
            .with(WATERLOGGED, context.world.getFluidState(context.blockPos) == Fluids.WATER)

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
                        handStack.tag == trade.price.tag
                val canInsertPayment = be.storage.canInsert(trade.price)

                if (trade.isEmpty()) {
                    player.addMessage(TranslatableText("block.adorn.trading_station.empty_trade"), true)
                } else if (!be.isStorageStocked()) {
                    player.addMessage(TranslatableText("block.adorn.trading_station.storage_not_stocked"), true)
                } else if (!canInsertPayment) {
                    player.addMessage(TranslatableText("block.adorn.trading_station.storage_full"), true)
                } else if (validPayment) {
                    handStack.decrement(trade.price.count)
                    player.giveItemStack(trade.selling.copy())
                    be.storage.tryExtract(trade.selling)
                    be.storage.tryInsert(trade.price)
                }
            } else {
                player.openFabricContainer(AdornGuis.TRADING_STATION, pos)
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
            player.openFabricContainer(AdornGuis.TRADING_STATION, pos)
            return ActionResult.SUCCESS
        }

        return ActionResult.PASS
    }

    override fun onBlockRemoved(state1: BlockState, world: World, pos: BlockPos, state2: BlockState, b: Boolean) {
        if (state1.block != state2.block) {
            val entity = world.getBlockEntity(pos)

            if (entity is TradingStationBlockEntity) {
                ItemScatterer.spawn(world, pos, entity.storage)
                world.updateComparators(pos, this)
            }

            super.onBlockRemoved(state1, world, pos, state2, b)
        }
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) =
        OUTLINE_SHAPE

    override fun getCollisionShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext) =
        COLLISION_SHAPE

    /**
     * Disables block breaking for non-owners.
     */
    override fun calcBlockBreakingDelta(state: BlockState, player: PlayerEntity, world: BlockView, pos: BlockPos) =
        if (AdornConfigManager.CONFIG.protectTradingStations)
            (world.getBlockEntity(pos) as? TradingStationBlockEntity).let {
                if (it != null && !it.isOwner(player)) {
                    0f
                } else {
                    super.calcBlockBreakingDelta(state, player, world, pos)
                }
            }
        else
            super.calcBlockBreakingDelta(state, player, world, pos)

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
