package juuxel.adorn.block.entity

import alexiil.mc.lib.attributes.item.impl.SimpleFixedItemInv
import juuxel.adorn.block.KitchenCupboardBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.container.Container
import net.minecraft.container.ContainerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.CompoundTag

class KitchenCupboardBlockEntity : BlockEntity(KitchenCupboardBlock.BLOCK_ENTITY_TYPE), ContainerProvider {
    val inventory = SimpleFixedItemInv(9)

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        inventory.toTag(tag)
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        inventory.fromTag(tag)
    }

    override fun createMenu(syncId: Int, playerInv: PlayerInventory, player: PlayerEntity): Container {
        TODO("add gui") //To change body of created functions use File | Settings | File Templates.
    }
}
