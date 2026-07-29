package juuxel.adorn.block.variant;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.datafixers.util.Pair;
import juuxel.adorn.block.BenchBlock;
import juuxel.adorn.block.ChairBlock;
import juuxel.adorn.block.CoffeeTableBlock;
import juuxel.adorn.block.DrawerBlock;
import juuxel.adorn.block.KitchenCounterBlock;
import juuxel.adorn.block.KitchenCupboardBlock;
import juuxel.adorn.block.KitchenSinkBlock;
import juuxel.adorn.block.PlatformBlock;
import juuxel.adorn.block.PostBlock;
import juuxel.adorn.block.ShelfBlock;
import juuxel.adorn.block.StepBlock;
import juuxel.adorn.block.TableBlock;
import juuxel.adorn.item.ChairBlockItem;
import juuxel.adorn.item.TableBlockItem;
import juuxel.adorn.lib.registry.BlockRegistrar;
import juuxel.adorn.lib.registry.ItemRegistrar;
import juuxel.adorn.lib.registry.RegisteredBlock;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.lib.registry.RegistryHelper;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BlockVariantSets {
    public static final BlockRegistrar BLOCKS = RegistrarFactory.get().createBlocks();
    public static final ItemRegistrar ITEMS = RegistrarFactory.get().createItems();
    private static final RegistryHelper HELPER = new RegistryHelper(BLOCKS, ITEMS);

    private static final List<BlockVariantSet> variantSets = new ArrayList<>();
    private static final ListMultimap<BlockKind, RegisteredBlock<Block>> blocksByKind =
        MultimapBuilder.enumKeys(BlockKind.class)
            .arrayListValues()
            .build();
    private static final ListMultimap<BlockVariant, RegisteredBlock<Block>> blocksByVariant =
        MultimapBuilder.linkedHashKeys()
            .arrayListValues()
            .build();
    private static final Map<Pair<BlockKind, BlockVariant>, RegisteredBlock<Block>> blocksByKindVariant = new LinkedHashMap<>();

    static {
        variantSets.add(new MinecraftBlockVariants());
    }

    public static Set<BlockVariant> allVariants() {
        var variants = new ArrayList<>(blocksByVariant.keySet());
        BlockVariantSet.Sorter sorter = (variant, after) -> {
            variants.remove(variant);
            variants.add(variants.indexOf(after) + 1, variant);
        };
        for (var variantSet : variantSets) {
            variantSet.sortVariants(sorter);
        }
        return new LinkedHashSet<>(variants);
    }

    public static void add(BlockVariantSet variantSet) {
        variantSets.add(variantSet);
    }

    public static List<RegisteredBlock<Block>> get(BlockKind kind) {
        return blocksByKind.get(kind);
    }

    public static @Nullable RegisteredBlock<Block> get(BlockKind kind, BlockVariant variant) {
        return blocksByKindVariant.get(Pair.of(kind, variant));
    }

    public static void register() {
        var woodVariants = variantSets.stream().flatMap(set -> set.getWoodVariants().stream()).toList();
        var stoneVariants = variantSets.stream().flatMap(set -> set.getStoneVariants().stream()).toList();
        var allVariants = new ArrayList<BlockVariant>(woodVariants.size() + stoneVariants.size());
        allVariants.addAll(woodVariants);
        allVariants.addAll(stoneVariants);
        register(BlockKind.CHAIR, woodVariants);
        register(BlockKind.TABLE, woodVariants);
        register(BlockKind.DRAWER, woodVariants);
        register(BlockKind.KITCHEN_COUNTER, woodVariants);
        register(BlockKind.KITCHEN_CUPBOARD, woodVariants);
        register(BlockKind.KITCHEN_SINK, woodVariants);
        register(BlockKind.POST, allVariants);
        register(BlockKind.PLATFORM, allVariants);
        register(BlockKind.STEP, allVariants);
        register(BlockKind.SHELF, woodVariants);
        register(BlockKind.COFFEE_TABLE, woodVariants);
        register(BlockKind.BENCH, woodVariants);

        for (var set : variantSets) {
            set.addVariants((variant, kinds) -> {
                for (var kind : kinds) {
                    register(kind, variant);
                }
            });
        }
    }

    private static void register(BlockKind kind, List<BlockVariant> variants) {
        for (var variant : variants) {
            register(kind, variant);
        }
    }

    private static void register(BlockKind kind, BlockVariant variant) {
        var registered = switch (kind) {
            case CHAIR -> registerChair(variant);
            case TABLE -> registerTable(variant);
            case DRAWER -> registerDrawer(variant);
            case KITCHEN_COUNTER -> registerKitchenCounter(variant);
            case KITCHEN_CUPBOARD -> registerKitchenCupboard(variant);
            case KITCHEN_SINK -> registerKitchenSink(variant);
            case POST -> registerPost(variant);
            case PLATFORM -> registerPlatform(variant);
            case STEP -> registerStep(variant);
            case SHELF -> registerShelf(variant);
            case COFFEE_TABLE -> registerCoffeeTable(variant);
            case BENCH -> registerBench(variant);
        };
        blocksByKind.put(kind, registered);
        blocksByVariant.put(variant, registered);
        blocksByKindVariant.put(Pair.of(kind, variant), registered);
    }

    private static RegisteredBlock<Block> registerPost(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_post", PostBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerPlatform(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_platform", PlatformBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerStep(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_step", StepBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerDrawer(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_drawer", DrawerBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerChair(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_chair", ChairBlockItem::new, ChairBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerTable(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_table", TableBlockItem::new, TableBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerKitchenCounter(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_kitchen_counter", KitchenCounterBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerKitchenCupboard(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_kitchen_cupboard", KitchenCupboardBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerKitchenSink(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_kitchen_sink", KitchenSinkBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerShelf(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_shelf", ShelfBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerCoffeeTable(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_coffee_table", CoffeeTableBlock::new, variant);
    }

    private static RegisteredBlock<Block> registerBench(BlockVariant variant) {
        return HELPER.registerBlock(variant.name() + "_bench", BenchBlock::new, variant);
    }
}
