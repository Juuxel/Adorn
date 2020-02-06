package juuxel.adorn.gui

import juuxel.adorn.Adorn
import juuxel.adorn.gui.screen.DrawerScreen
import juuxel.adorn.gui.screen.KitchenCupboardScreen
import juuxel.adorn.gui.screen.TradingStationScreen
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.client.gui.screen.ingame.ContainerScreen
import net.minecraft.container.Container
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

object AdornGuis {
    val DRAWER = Adorn.id("drawer")
    val KITCHEN_CUPBOARD = Adorn.id("kitchen_cupboard")
    val TRADING_STATION = Adorn.id("trading_station")

    fun init() {
        registerContainer(DRAWER)
        registerContainer(KITCHEN_CUPBOARD)
        registerContainer(TRADING_STATION)
    }

    @Environment(EnvType.CLIENT)
    fun initClient() {
        registerScreen(DRAWER, ::DrawerScreen)
        registerScreen(KITCHEN_CUPBOARD, ::KitchenCupboardScreen)
        registerScreen(TRADING_STATION, ::TradingStationScreen)
    }

    private fun registerContainer(
        id: Identifier
    ) = ContainerProviderRegistry.INSTANCE.registerFactory(id) { syncId, _, player, buf ->
        val world = player.world
        val pos = buf.readBlockPos()
        val provider = world.getBlockState(pos).createContainerFactory(world, pos)
        provider?.createMenu(syncId, player.inventory, player)
    }

    private inline fun <reified C : Container> registerScreen(
        id: Identifier,
        crossinline screenFn: (C, PlayerEntity) -> ContainerScreen<in C>
    ) = ScreenProviderRegistry.INSTANCE.registerFactory(id) { syncId, _, player, buf ->
        val world = player.world
        val pos = buf.readBlockPos()
        val provider = world.getBlockState(pos).createContainerFactory(world, pos)
        provider?.let {
            screenFn(it.createMenu(syncId, player.inventory, player) as C, player)
        }
    }
}

fun PlayerEntity.openFabricContainer(id: Identifier, pos: BlockPos) {
    if (!world.isClient) {
        ContainerProviderRegistry.INSTANCE.openContainer(id, this) { it.writeBlockPos(pos) }
    }
}
