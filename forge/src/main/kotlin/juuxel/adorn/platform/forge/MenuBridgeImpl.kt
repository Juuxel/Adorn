package juuxel.adorn.platform.forge

import juuxel.adorn.lib.Registered
import juuxel.adorn.platform.MenuBridge
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.MenuType
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraftforge.network.IContainerFactory
import net.minecraftforge.network.NetworkHooks
import net.minecraftforge.registries.ForgeRegistries
import org.apache.logging.log4j.LogManager

object MenuBridgeImpl : MenuBridge {
    private val LOGGER = LogManager.getLogger()
    @JvmField
    val MENUS = RegistrarImpl(ForgeRegistries.CONTAINERS)

    override fun open(player: PlayerEntity, factory: NamedMenuFactory?, pos: BlockPos) {
        if (factory == null) {
            LOGGER.warn("[Adorn] Menu factory is null, please report this!", Throwable("Stacktrace").fillInStackTrace())
            return
        }

        if (player is ServerPlayerEntity) {
            NetworkHooks.openGui(player, factory, pos)
        }
    }

    override fun <M : Menu> register(id: String, factory: (syncId: Int, inventory: PlayerInventory) -> M): Registered<MenuType<M>> =
        MENUS.register(id) { MenuType(factory) }

    override fun <M : Menu> register(id: String, factory: (Int, PlayerInventory, PacketByteBuf) -> M): Registered<MenuType<M>> =
        MENUS.register(id) { MenuType(IContainerFactory(factory)) }
}
