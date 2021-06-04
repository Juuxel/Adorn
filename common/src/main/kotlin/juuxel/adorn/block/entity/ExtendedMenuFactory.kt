package juuxel.adorn.block.entity

import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity

interface ExtendedMenuFactory : NamedScreenHandlerFactory {
    fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf)
}
