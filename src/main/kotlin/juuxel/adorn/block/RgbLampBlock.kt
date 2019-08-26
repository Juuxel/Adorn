package juuxel.adorn.block

import juuxel.adorn.block.entity.RgbLampBlockEntity
import juuxel.adorn.lib.ModGuis
import juuxel.adorn.lib.openFabricContainer
import juuxel.polyester.block.PolyesterBlockEntityType
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.RedstoneLampBlock
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.NameableContainerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class RgbLampBlock : RedstoneLampBlock(Settings.copy(Blocks.REDSTONE_LAMP)), BlockEntityProvider {
    override fun createBlockEntity(world: BlockView) = RgbLampBlockEntity()

    override fun createContainerProvider(state: BlockState, world: World, pos: BlockPos) =
        world.getBlockEntity(pos) as? NameableContainerProvider

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        player.openFabricContainer(ModGuis.RGB_LAMP, pos)
        return true
    }

    companion object {
        val BLOCK_ENTITY_TYPE: BlockEntityType<RgbLampBlockEntity> = PolyesterBlockEntityType(::RgbLampBlockEntity)
    }
}
