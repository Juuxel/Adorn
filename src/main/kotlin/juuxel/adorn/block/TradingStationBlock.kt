package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlockWithEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.lib.ModGuis
import juuxel.adorn.util.shapeRotations
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.VerticalEntityPosition
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class TradingStationBlock : PolyesterBlockWithEntity(Settings.copy(Blocks.CRAFTING_TABLE)) {
    override val name = "trading_station"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)
    override val blockEntityType = BLOCK_ENTITY_TYPE

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.with(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite)

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack?) {
        if (entity is PlayerEntity) {
            val be = world.getBlockEntity(pos) as? TradingStationBlockEntity ?: return
            be.setOwner(entity)
        }
    }

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult?
    ): Boolean {
        val be = world.getBlockEntity(pos)
        if (!world.isClient && be is TradingStationBlockEntity) {
            if (be.owner == null) {
                be.setOwner(player)
            }

            if (player.gameProfile.id != be.owner) {
                val handStack = player.getStackInHand(hand)
                val trade = be.trade
                val validPayment = handStack.isEqualIgnoreTags(trade.price) && handStack.amount >= trade.price.amount
                val canInsertPayment = be.storage.canInsert(trade.price)

                if (validPayment && be.isStorageStocked() && canInsertPayment) {
                    handStack.subtractAmount(trade.price.amount)
                    player.giveItemStack(trade.selling.copy())
                    be.storage.tryExtract(trade.selling)
                    be.storage.tryInsert(trade.price)
                }
            } else {
                ContainerProviderRegistry.INSTANCE.openContainer(ModGuis.TRADING_TABLE, player) { buf ->
                    buf.writeBlockPos(pos)
                }
            }
        }

        return true
    }

    override fun onBlockRemoved(state1: BlockState, world: World, pos: BlockPos, state2: BlockState, b: Boolean) {
        if (state1.block != state2.block) {
            val entity = world.getBlockEntity(pos)

            if (entity is TradingStationBlockEntity) {
                ItemScatterer.spawn(world, pos, entity.storage)
                world.updateHorizontalAdjacent(pos, this)
            }

            super.onBlockRemoved(state1, world, pos, state2, b)
        }
    }

    override fun getOutlineShape(state: BlockState, view: BlockView?, pos: BlockPos?, vep: VerticalEntityPosition?) =
        SHAPES[state[FACING]]

    companion object {
        val BLOCK_ENTITY_TYPE = BlockEntityType(::TradingStationBlockEntity, null)
        val FACING = Properties.FACING_HORIZONTAL
        private val SHAPES: Map<Direction, VoxelShape>

        init {
            val top = createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0)
            val front = shapeRotations(13, 0, 0, 15, 12, 16)
            val north = shapeRotations(0, 0, 0, 13, 12, 2)
            val south = shapeRotations(0, 0, 14, 13, 12, 16)

            SHAPES = Direction.values().filter { it.horizontal != -1 }.map {
                it to VoxelShapes.union(top, front[it], north[it], south[it])
            }.toMap()
        }
    }
}
