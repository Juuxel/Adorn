package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.lib.ModGuis
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class DrawerBlock(
    material: String
) : Block(Settings.copy(Blocks.OAK_PLANKS)), PolyesterBlock, BlockEntityProvider, BaseInventoryBlockEntity.InventoryProviderImpl {
    override val name = "${material}_drawer"
    override val itemSettings = Item.Settings().itemGroup(ItemGroup.DECORATIONS)

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.with(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(FACING, context.playerHorizontalFacing.opposite)

    override fun isFullBoundsCubeForCulling(state: BlockState?) = false
    override fun isSimpleFullBlock(state: BlockState?, view: BlockView?, pos: BlockPos?) = false
    override fun createBlockEntity(var1: BlockView?) = BLOCK_ENTITY_TYPE.instantiate()

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        if (!world.isClient && world.getBlockEntity(pos) is DrawerBlockEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(ModGuis.DRAWER, player) { buf ->
                buf.writeBlockPos(pos)
            }
        }

        return true
    }

    override fun onBlockRemoved(state1: BlockState, world: World, pos: BlockPos, state2: BlockState, b: Boolean) {
        if (state1.block != state2.block) {
            val entity = world.getBlockEntity(pos)

            if (entity is BaseInventoryBlockEntity) {
                ItemScatterer.spawn(world, pos, getInventory(state1, world, pos))
                world.updateHorizontalAdjacent(pos, this)
            }

            super.onBlockRemoved(state1, world, pos, state2, b)
        }
    }

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        val BLOCK_ENTITY_TYPE: BlockEntityType<DrawerBlockEntity> =
            BlockEntityType(::DrawerBlockEntity, null)
    }
}
