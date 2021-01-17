package juuxel.adorn.block

import juuxel.adorn.Adorn
import juuxel.adorn.block.entity.AdornBlockEntityType
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity
import juuxel.adorn.block.entity.ShelfBlockEntity
import juuxel.adorn.block.entity.TradingStationBlockEntity
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.ForgeRegistries

object AdornBlockEntities {
    val BLOCK_ENTITIES = Adorn.register(ForgeRegistries.TILE_ENTITIES)

    val SHELF = register<ShelfBlock, ShelfBlockEntity>("shelf", ::ShelfBlockEntity)
    val DRAWER = register<DrawerBlock, DrawerBlockEntity>("drawer", ::DrawerBlockEntity)
    val KITCHEN_CUPBOARD = register<KitchenCupboardBlock, KitchenCupboardBlockEntity>(
        "kitchen_cupboard", ::KitchenCupboardBlockEntity
    )
    val TRADING_STATION = register<TradingStationBlock, TradingStationBlockEntity>(
        "trading_station", ::TradingStationBlockEntity
    )

    private inline fun <reified B : Block, E : BlockEntity> register(
        name: String, noinline factory: () -> E
    ): RegistryObject<BlockEntityType<E>> =
        BLOCK_ENTITIES.register(name) {
            AdornBlockEntityType(factory, blockPredicate = { it is B })
        }
}
