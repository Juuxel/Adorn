package juuxel.adorn.lib

import juuxel.adorn.Adorn
import juuxel.adorn.gui.controller.DrawerController
import juuxel.adorn.gui.controller.KitchenCupboardController
import juuxel.adorn.gui.screen.DrawerScreen
import juuxel.adorn.gui.screen.KitchenCupboardScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.client.gui.ContainerScreen
import net.minecraft.container.BlockContext
import net.minecraft.container.Container
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.Identifier

object ModGuis {
    val DRAWER = Identifier(Adorn.NAMESPACE, "drawer")
    val KITCHEN_CUPBOARD = Identifier(Adorn.NAMESPACE, "kitchen_cupboard")

    fun init() {
        registerContainer(DRAWER, ::DrawerController)
        registerContainer(KITCHEN_CUPBOARD, ::KitchenCupboardController)
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        registerScreen(DRAWER, ::DrawerController, ::DrawerScreen)
        registerScreen(KITCHEN_CUPBOARD, ::KitchenCupboardController, ::KitchenCupboardScreen)
    }

    private inline fun registerContainer(id: Identifier, crossinline fn: (Int, PlayerInventory, BlockContext) -> Container) =
        ContainerProviderRegistry.INSTANCE.registerFactory(id) { syncId, _, player, buf ->
            fn(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos()))
        }

    private inline fun <C : Container> registerScreen(
        id: Identifier,
        crossinline containerFn: (Int, PlayerInventory, BlockContext) -> C,
        crossinline screenFn: (C, PlayerEntity) -> ContainerScreen<in C>
    ) =
        ScreenProviderRegistry.INSTANCE.registerFactory(id) { syncId, _, player, buf ->
            screenFn(
                containerFn(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())),
                player
            )
        }
}
