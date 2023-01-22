package juuxel.adorn.menu

import juuxel.adorn.block.AdornBlocks
import juuxel.adorn.block.entity.FurnitureWorkbenchBlockEntity
import juuxel.adorn.design.FurniturePart
import juuxel.adorn.util.getBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuContext
import net.minecraft.network.PacketByteBuf
import net.minecraft.world.World

class FurnitureWorkbenchMenu(
    syncId: Int,
    playerInventory: PlayerInventory,
    private val context: MenuContext
) : Menu(AdornMenus.FURNITURE_WORKBENCH, syncId), MenuWithDataChannels {
    private val blockEntity = context.getBlockEntity() as FurnitureWorkbenchBlockEntity
    private val world: World = playerInventory.player.world

    override val dataChannels = DataChannels()
    var furnitureParts: MutableList<FurniturePart>
        get() = blockEntity.furnitureParts
        private set(value) {
            blockEntity.furnitureParts = value
        }
    val partChannel: DataChannel<MutableList<FurniturePart>> = dataChannels.register(
        DataChannel(
            playerInventory.player,
            syncId,
            id = 0,
            getter = { furnitureParts },
            setter = { furnitureParts = it },
            copier = { furnitureParts.mapTo(ArrayList()) { it.deepCopy() } },
            reader = { buf -> buf.readCollection(::ArrayList, FurniturePart::load) },
            writer = { buf, parts -> buf.writeCollection(parts, FurniturePart::write) }
        )
    )

    fun addCuboid() {
        if (!world.isClient) {
            throw IllegalStateException("addCuboid must only be called on the client!")
        }
    }

    override fun quickMove(player: PlayerEntity, slot: Int): ItemStack {
        TODO("Not yet implemented")
    }

    override fun canUse(player: PlayerEntity): Boolean =
        canUse(context, player, AdornBlocks.FURNITURE_WORKBENCH)

    override fun sendContentUpdates() {
        super.sendContentUpdates()
        dataChannels.tick()
    }

    companion object {
        fun load(syncId: Int, playerInventory: PlayerInventory, buf: PacketByteBuf): FurnitureWorkbenchMenu {
            val context = MenuContext.create(playerInventory.player.world, buf.readBlockPos())
            return FurnitureWorkbenchMenu(syncId, playerInventory, context)
        }
    }
}
