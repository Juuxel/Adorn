package juuxel.adorn.block

import juuxel.adorn.block.entity.RgbLampBlockEntity
import juuxel.adorn.lib.ModGuis
import juuxel.adorn.lib.ModNetworking
import juuxel.adorn.lib.openFabricContainer
import juuxel.adorn.network.RgbLampColorUpdateS2CPacket
import juuxel.polyester.block.PolyesterBlockEntityType
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.NameableContainerProvider
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class RgbLampBlock : RedstoneLampBlock(Settings.copy(Blocks.REDSTONE_LAMP)), BlockEntityProvider, Colorable {
    override fun createBlockEntity(world: BlockView) = RgbLampBlockEntity()

    override fun createContainerProvider(state: BlockState, world: World, pos: BlockPos) =
        world.getBlockEntity(pos) as? NameableContainerProvider

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        player.openFabricContainer(ModGuis.RGB_LAMP, pos)
        return true
    }

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (world is ServerWorld) {
            val be = world.getBlockEntity(pos) as? RgbLampBlockEntity ?: return
            PlayerStream.watching(be).forEach { player ->
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(
                    player,
                    ModNetworking.RGB_LAMP_COLOR_UPDATE_S2C,
                    RgbLampColorUpdateS2CPacket(pos, be.red, be.green, be.blue).toBuf()
                )
            }
        }
    }

    override fun applyColor(world: World, pos: BlockPos, state: BlockState, red: Int, green: Int, blue: Int) {
        (world.getBlockEntity(pos) as? RgbLampBlockEntity)?.let { be ->
            be.red = red
            be.green = green
            be.blue = blue
            be.markDirty()
            PlayerStream.watching(be).forEach { player ->
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(
                    player,
                    ModNetworking.RGB_LAMP_COLOR_UPDATE_S2C,
                    RgbLampColorUpdateS2CPacket(pos, be.red, be.green, be.blue).toBuf()
                )
            }
        }
    }

    override fun getRenderLayer() = BlockRenderLayer.TRANSLUCENT

    companion object {
        val BLOCK_ENTITY_TYPE: BlockEntityType<RgbLampBlockEntity> = PolyesterBlockEntityType(::RgbLampBlockEntity)
    }
}
