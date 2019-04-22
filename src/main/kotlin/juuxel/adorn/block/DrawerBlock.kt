package juuxel.adorn.block

import io.github.juuxel.polyester.registry.PolyesterBlock
import juuxel.adorn.block.entity.BaseInventoryBlockEntity
import juuxel.adorn.block.entity.DrawerBlockEntity
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

class DrawerBlock(
    material: String
) : Block(Settings.copy(Blocks.OAK_PLANKS)), PolyesterBlock, BlockEntityProvider, BaseInventoryBlockEntity.BlockAttributeProviderImpl {
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

    companion object {
        val FACING = Properties.FACING_HORIZONTAL
        val BLOCK_ENTITY_TYPE: BlockEntityType<DrawerBlockEntity> =
            BlockEntityType(::DrawerBlockEntity, null)
    }
}
