package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlockWithEntity
import juuxel.adorn.block.entity.TradingTableBlockEntity
import juuxel.adorn.lib.ModGuis
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class TradingTableBlock : PolyesterBlockWithEntity(Settings.copy(Blocks.CRAFTING_TABLE)) {
    override val name = "trading_table"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)
    override val blockEntityType = BLOCK_ENTITY_TYPE

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack?) {
        if (entity is PlayerEntity) {
            val be = world.getBlockEntity(pos) as? TradingTableBlockEntity ?: return
            be.setOwner(entity)
        }
    }

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult?
    ): Boolean {
        val be = world.getBlockEntity(pos)
        if (!world.isClient && be is TradingTableBlockEntity) {
            if (be.owner == null) {
                be.setOwner(player)
            }

            if (player.gameProfile.id != be.owner) {
                val handStack = player.getStackInHand(hand)
                val trade = be.trade
                val validPayment = handStack.isEqualIgnoreTags(trade.price) && handStack.amount >= trade.price.amount
                val canInsertPayment = be.storage.canInsert(trade.price)

                if (validPayment && be.isStorageStocked() && canInsertPayment) {
                    world.playSound(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), SoundEvents.ENTITY_VILLAGER_TRADE, SoundCategory.BLOCKS, 1f, 1f, false)
                    handStack.subtractAmount(trade.price.amount)
                    player.giveItemStack(trade.selling.copy())
                    be.storage.tryExtract(trade.selling)
                    be.storage.tryInsert(trade.price)
                } else {
                    world.playSound(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1f, 1f, false)
                }
            } else {
                ContainerProviderRegistry.INSTANCE.openContainer(ModGuis.TRADING_TABLE, player) { buf ->
                    buf.writeBlockPos(pos)
                }
            }
        }

        return true
    }

    companion object {
        val BLOCK_ENTITY_TYPE = BlockEntityType(::TradingTableBlockEntity, null)
    }
}
