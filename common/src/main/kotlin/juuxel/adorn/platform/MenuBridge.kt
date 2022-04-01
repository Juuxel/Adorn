package juuxel.adorn.platform

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuType
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.BlockPos

interface MenuBridge {
    // This is only needed because Forge wants to be special and have a custom opening method.
    /**
     * Opens a menu with a [pos] with the opening NBT sent to the client.
     * Does nothing on the client.
     *
     * @param player  the player opening the menu
     * @param factory the menu factory
     */
    fun open(player: PlayerEntity, factory: NamedMenuFactory?, pos: BlockPos)

    fun <M : Menu> create(factory: (syncId: Int, inventory: PlayerInventory) -> M): MenuType<M>
    fun <M : Menu> create(factory: (syncId: Int, inventory: PlayerInventory, buf: PacketByteBuf) -> M): MenuType<M>
}
