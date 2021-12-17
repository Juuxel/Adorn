package juuxel.adorn.platform

import juuxel.adorn.lib.Registered
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
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
    fun open(player: PlayerEntity, factory: NamedScreenHandlerFactory?, pos: BlockPos)

    fun <M : ScreenHandler> register(id: String, factory: (syncId: Int, inventory: PlayerInventory) -> M): Registered<ScreenHandlerType<M>>
    fun <M : ScreenHandler> register(id: String, factory: (syncId: Int, inventory: PlayerInventory, buf: PacketByteBuf) -> M): Registered<ScreenHandlerType<M>>
}
