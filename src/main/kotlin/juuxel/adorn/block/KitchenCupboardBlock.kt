package juuxel.adorn.block

import io.github.juuxel.polyester.block.BlockEntityProviderImpl
import io.github.juuxel.polyester.block.PolyesterBlockEntityType
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.lib.ModGuis
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
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
        if (!world.isClient && world.getBlockEntity(pos) is KitchenCupboardBlockEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(ModGuis.KITCHEN_CUPBOARD, player) { buf ->
                buf.writeBlockPos(pos)
            }
        }

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

    companion object {
        val BLOCK_ENTITY_TYPE: BlockEntityType<KitchenCupboardBlockEntity> =
            PolyesterBlockEntityType(::KitchenCupboardBlockEntity)
    }
}
