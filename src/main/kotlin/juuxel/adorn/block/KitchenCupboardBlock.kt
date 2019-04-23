package juuxel.adorn.block

import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.lib.ModGuis
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class KitchenCupboardBlock(
    material: String
) : BaseKitchenCounterBlock(), BlockEntityProvider, BaseInventoryBlockEntity.BlockAttributeProviderImpl {
    override val name = "${material}_kitchen_cupboard"

    override fun createBlockEntity(view: BlockView?) = BLOCK_ENTITY_TYPE.instantiate()

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

    companion object {
        val BLOCK_ENTITY_TYPE: BlockEntityType<KitchenCupboardBlockEntity> =
            BlockEntityType(::KitchenCupboardBlockEntity, null)
    }
}
