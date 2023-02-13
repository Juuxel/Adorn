package juuxel.adorn.block

import juuxel.adorn.block.entity.AdornBlockEntityType
import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.KitchenSinkBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import juuxel.adorn.lib.registry.RegistrarFactory
import juuxel.adorn.platform.PlatformBridges
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.math.BlockPos

object AdornBlockEntities {
    @JvmField
    val BLOCK_ENTITIES = RegistrarFactory.get().create(RegistryKeys.BLOCK_ENTITY_TYPE)

    /* ktlint-disable max-line-length */
    val SHELF: BlockEntityType<ShelfBlockEntity> by register("shelf", ::ShelfBlockEntity, ShelfBlock::class.java)
    val DRAWER: BlockEntityType<DrawerBlockEntity> by register("drawer", ::DrawerBlockEntity, DrawerBlock::class.java)
    val KITCHEN_CUPBOARD: BlockEntityType<KitchenCupboardBlockEntity> by register("kitchen_cupboard", ::KitchenCupboardBlockEntity, KitchenCupboardBlock::class.java)
    val KITCHEN_SINK: BlockEntityType<KitchenSinkBlockEntity> by register("kitchen_sink", PlatformBridges.blockEntities::createKitchenSink, KitchenSinkBlock::class.java)
    val TRADING_STATION: BlockEntityType<TradingStationBlockEntity> by register("trading_station", ::TradingStationBlockEntity, AdornBlocks::TRADING_STATION)
    val BREWER: BlockEntityType<BrewerBlockEntity> by register("brewer", PlatformBridges.blockEntities::createBrewer, AdornBlocks::BREWER)
    /* ktlint-enable max-line-length */

    private fun <E : BlockEntity> register(name: String, factory: (BlockPos, BlockState) -> E, block: () -> Block) =
        BLOCK_ENTITIES.register(name) { BlockEntityType.Builder.create(factory, block()).build(null) }

    private fun <E : BlockEntity> register(name: String, factory: (BlockPos, BlockState) -> E, blockClass: Class<out Block>) =
        BLOCK_ENTITIES.register(name) { AdornBlockEntityType(factory, blockClass::isInstance) }

    fun init() {}
}
