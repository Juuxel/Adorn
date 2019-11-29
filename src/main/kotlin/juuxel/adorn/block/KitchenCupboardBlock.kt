package juuxel.adorn.block

import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.block.entity.BETypeProvider
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.gui.AdornGuis
import juuxel.adorn.gui.openFabricContainer
import juuxel.adorn.block.entity.MutableBlockEntityType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.NameableContainerProvider
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class KitchenCupboardBlock : BaseKitchenCounterBlock, BETypeProvider, BaseInventoryBlockEntity.InventoryProviderImpl {
    constructor() : super()
    constructor(variant: BlockVariant) : super(variant.createSettings())

    override val blockEntityType = BLOCK_ENTITY_TYPE

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): ActionResult {
        player.openFabricContainer(AdornGuis.KITCHEN_CUPBOARD, pos)
        return ActionResult.SUCCESS
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
            MutableBlockEntityType(::KitchenCupboardBlockEntity)
    }
}
