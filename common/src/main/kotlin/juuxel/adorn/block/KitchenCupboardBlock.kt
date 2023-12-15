@file:Suppress("DEPRECATION")

package juuxel.adorn.block

import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.SimpleContainerBlockEntity
import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class KitchenCupboardBlock(variant: BlockVariant) : AbstractKitchenCounterBlock(variant), BlockEntityProvider, BlockWithDescription {
    override val descriptionKey = "block.adorn.kitchen_cupboard.description"

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is KitchenCupboardBlockEntity) {
            PlatformBridges.menus.open(player, blockEntity, pos)
            player.incrementStat(AdornStats.OPEN_KITCHEN_CUPBOARD)
        }

        return ActionResult.CONSUME
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        ItemScatterer.onStateReplaced(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (stack.hasCustomName()) {
            (world.getBlockEntity(pos) as? LootableContainerBlockEntity)?.customName = stack.name
        }
    }

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        Menu.calculateComparatorOutput(world.getBlockEntity(pos))

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.KITCHEN_CUPBOARD.instantiate(pos, state)

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val entity = world.getBlockEntity(pos)
        if (entity is SimpleContainerBlockEntity) {
            entity.onScheduledTick()
        }
    }
}
