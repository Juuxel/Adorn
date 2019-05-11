package juuxel.adorn.lib

import io.github.juuxel.polyester.container.ContainerRegistry
import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.gui.controller.DrawerController
import juuxel.adorn.gui.controller.KitchenCupboardController
import juuxel.adorn.gui.controller.TradingStationController
import juuxel.adorn.gui.screen.DrawerScreen
import juuxel.adorn.gui.screen.KitchenCupboardScreen
import juuxel.adorn.gui.screen.TradingStationScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.ContainerScreen
import net.minecraft.container.BlockContext
import net.minecraft.container.Container
import net.minecraft.container.ContainerType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.chat.Component
import net.minecraft.util.registry.Registry

object ModGuis : PolyesterRegistry(Adorn.NAMESPACE) {
    val DRAWER = registerContainer("drawer", ::DrawerController)
    val KITCHEN_CUPBOARD = registerContainer("kitchen_cupboard", ::KitchenCupboardController)
    val TRADING_STATION = registerContainer("trading_station", ::TradingStationController)

    fun init() {}

    @Environment(EnvType.CLIENT)
    fun initClient() {
        registerScreen(DRAWER, ::DrawerScreen)
        registerScreen(KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        registerScreen(TRADING_STATION, ::TradingStationScreen)
    }

    private inline fun <C : Container> registerContainer(name: String, crossinline fn: (Int, PlayerInventory, BlockContext) -> C) =
        register(Registry.CONTAINER, name, ContainerRegistry.INSTANCE.createContainerType { syncId, playerInv ->
            fn(syncId, playerInv, BlockContext.EMPTY)
        })

    private inline fun <C : Container> registerScreen(type: ContainerType<C>, crossinline fn: (C, PlayerEntity, Component) -> ContainerScreen<C>) =
        ContainerRegistry.INSTANCE.registerScreen(type) { container, playerInventory, title ->
            fn(container, playerInventory.player, title)
        }
}
