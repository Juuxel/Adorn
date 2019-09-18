package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.gui.AdornGuis
import juuxel.adorn.gui.openFabricContainer
import juuxel.adorn.block.entity.MutableBlockEntityType
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

open class DrawerBlock(
    variant: BlockVariant
) : VisibleBlockWithEntity(variant.createSettings()), BaseInventoryBlockEntity.InventoryProviderImpl {
    override val blockEntityType = BLOCK_ENTITY_TYPE

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerFacing.opposite)

    override fun isOpaque(state: BlockState?) = false
    override fun isSimpleFullBlock(state: BlockState?, view: BlockView?, pos: BlockPos?) = false

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        player.openFabricContainer(AdornGuis.DRAWER, pos)
        return true
    }

    override fun onBlockRemoved(state1: BlockState, world: World, pos: BlockPos, state2: BlockState, b: Boolean) {
        if (state1.block != state2.block) {
            val entity = world.getBlockEntity(pos)

            if (entity is BaseInventoryBlockEntity) {
                ItemScatterer.spawn(world, pos, getInventory(state1, world, pos))
                world.updateHorizontalAdjacent(pos, this)
            }

            super.onBlockRemoved(state1, world, pos, state2, b)
        }
    }

    override fun mirror(state: BlockState, mirror: BlockMirror) =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun rotate(state: BlockState, rotation: BlockRotation) =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (stack.hasCustomName()) {
            (world.getBlockEntity(pos) as? DrawerBlockEntity)?.customName = stack.name
        }
    }

    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val BLOCK_ENTITY_TYPE: BlockEntityType<DrawerBlockEntity> =
            MutableBlockEntityType(::DrawerBlockEntity)
    }
}
