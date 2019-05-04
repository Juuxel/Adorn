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
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        val be = world.getBlockEntity(pos)
        if (!world.isClient && be is TradingTableBlockEntity) {
            if (be.owner == null) {
                be.setOwner(player)
            }

            if (player.gameProfile.id != be.owner) {
                return false
            }

            ContainerProviderRegistry.INSTANCE.openContainer(ModGuis.TRADING_TABLE, player) { buf ->
                buf.writeBlockPos(pos)
            }
        }

        return true
    }

    companion object {
        val BLOCK_ENTITY_TYPE = BlockEntityType(::TradingTableBlockEntity, null)
    }
}
