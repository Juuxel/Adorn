package juuxel.adorn.block;

import juuxel.adorn.block.entity.AdornBlockEntityType;
import juuxel.adorn.block.entity.BrewerBlockEntity;
import juuxel.adorn.block.entity.DrawerBlockEntity;
import juuxel.adorn.block.entity.KitchenCupboardBlockEntity;
import juuxel.adorn.block.entity.KitchenSinkBlockEntity;
import juuxel.adorn.block.entity.ShelfBlockEntity;
import juuxel.adorn.block.entity.TradingStationBlockEntity;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.Supplier;

public final class AdornBlockEntities {
    public static final Registrar<BlockEntityType<?>> BLOCK_ENTITIES = RegistrarFactory.get().create(Registries.BLOCK_ENTITY_TYPE);

    public static final Registered<BlockEntityType<ShelfBlockEntity>> SHELF = register("shelf", ShelfBlockEntity::new, ShelfBlock.class);
    public static final Registered<BlockEntityType<DrawerBlockEntity>> DRAWER = register("drawer", DrawerBlockEntity::new, DrawerBlock.class);
    public static final Registered<BlockEntityType<KitchenCupboardBlockEntity>> KITCHEN_CUPBOARD =
        register("kitchen_cupboard", KitchenCupboardBlockEntity::new, KitchenCupboardBlock.class);
    public static final Registered<BlockEntityType<KitchenSinkBlockEntity>> KITCHEN_SINK =
        register("kitchen_sink", PlatformBridges.get().getBlockEntities()::createKitchenSink, KitchenSinkBlock.class);
    public static final Registered<BlockEntityType<TradingStationBlockEntity>> TRADING_STATION =
        register("trading_station", TradingStationBlockEntity::new, AdornBlocks.TRADING_STATION);
    public static final Registered<BlockEntityType<BrewerBlockEntity>> BREWER =
        register("brewer", PlatformBridges.get().getBlockEntities()::createBrewer, AdornBlocks.BREWER);

    private static <E extends BlockEntity> Registered<BlockEntityType<E>> register(String name, BlockEntityType.BlockEntitySupplier<E> factory, Supplier<? extends Block> block) {
        return BLOCK_ENTITIES.register(name, () -> new BlockEntityType<>(factory, Set.of(block.get())));
    }

    private static <E extends BlockEntity> Registered<BlockEntityType<E>> register(String name, BlockEntityType.BlockEntitySupplier<E> factory, Class<? extends Block> blockClass) {
        return BLOCK_ENTITIES.register(name, () -> new AdornBlockEntityType<>(factory, blockClass::isInstance));
    }

    public static void init() {
    }
}
