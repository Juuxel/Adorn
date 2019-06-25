package juuxel.adorn.block

import io.github.juuxel.polyester.block.BlockEntityProviderImpl
import io.github.juuxel.polyester.block.PolyesterBlockEntityType
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.NameableContainerProvider
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class KitchenCupboardBlock(
    material: String
) : BaseKitchenCounterBlock(), BlockEntityProviderImpl, BaseInventoryBlockEntity.InventoryProviderImpl {
    override val name = "${material}_kitchen_cupboard"
    override val blockEntityType = BLOCK_ENTITY_TYPE

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        player.openContainer(state.createContainerProvider(world, pos))
        return true
    }

    override fun createContainerProvider(state: BlockState, world: World, pos: BlockPos) =
        world.getBlockEntity(pos) as? NameableContainerProvider

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

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (stack.hasCustomName()) {
            (world.getBlockEntity(pos) as? KitchenCupboardBlockEntity)?.customName = stack.name
        }
    }

    companion object {
        val BLOCK_ENTITY_TYPE: BlockEntityType<KitchenCupboardBlockEntity> =
            PolyesterBlockEntityType(::KitchenCupboardBlockEntity)
    }
}
