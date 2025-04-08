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
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.item.ChairBlockItem;
import juuxel.adorn.item.ExchangeValues;
import juuxel.adorn.item.TableBlockItem;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.lib.registry.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

public final class BlockVariantSets {
    public static final Registrar<Block> BLOCKS = RegistrarFactory.get().create(RegistryKeys.BLOCK);
    public static final Registrar<Item> ITEMS = RegistrarFactory.get().create(RegistryKeys.ITEM);
    private static final RegistryHelper HELPER = new RegistryHelper(BLOCKS, ITEMS);

    private static final List<BlockVariantSet> variantSets = new ArrayList<>();
    private static final ListMultimap<BlockKind, Registered<Block>> blocksByKind =
        MultimapBuilder.enumKeys(BlockKind.class)
            .arrayListValues()
            .build();
    private static final ListMultimap<BlockVariant, Registered<Block>> blocksByVariant =
        MultimapBuilder.linkedHashKeys()
            .arrayListValues()
            .build();
    private static final Map<Pair<BlockKind, BlockVariant>, Registered<Block>> blocksByKindVariant = new LinkedHashMap<>();

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

    public static void loadCompatSets() {
        for (var set : ServiceLoader.load(CompatBlockVariantSet.class)) {
            if (ConfigManager.isCompatEnabled(set.getModId())) {
                add(set);
            }
        }
    }

    public static List<Registered<Block>> get(BlockKind kind) {
        return blocksByKind.get(kind);
    }

    public static @Nullable Registered<Block> get(BlockKind kind, BlockVariant variant) {
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

    private static Registered<Block> registerPost(BlockVariant variant) {
        float value = ExchangeValues.round(0.25f * (variant.exchangeValue() + 2 * ExchangeValues.STICK));
        return HELPER.registerBlock(variant.name() + "_post", value, PostBlock::new, variant);
    }

    private static Registered<Block> registerPlatform(BlockVariant variant) {
        float value = ExchangeValues.round(0.5f * (ExchangeValues.SLAB + 0.25f * (variant.exchangeValue() + 2 * ExchangeValues.STICK)));
        return HELPER.registerBlock(variant.name() + "_platform", value, PlatformBlock::new, variant);
    }

    private static Registered<Block> registerStep(BlockVariant variant) {
        float value = ExchangeValues.SLAB + ExchangeValues.STICK;
        return HELPER.registerBlock(variant.name() + "_step", value, StepBlock::new, variant);
    }

    private static Registered<Block> registerDrawer(BlockVariant variant) {
        float value = ExchangeValues.round(0.5f * (2 * ExchangeValues.SLAB + ExchangeValues.CHEST));
        return HELPER.registerBlock(variant.name() + "_drawer", value, DrawerBlock::new, variant);
    }

    private static Registered<Block> registerChair(BlockVariant variant) {
        float value = ExchangeValues.round(0.5f * (3 * ExchangeValues.SLAB + 2 * ExchangeValues.STICK));
        return HELPER.registerBlock(variant.name() + "_chair", ChairBlockItem::new, value, ChairBlock::new, variant);
    }

    private static Registered<Block> registerTable(BlockVariant variant) {
        float value = ExchangeValues.round(0.33f * (3 * ExchangeValues.SLAB + 4 * ExchangeValues.STICK));
        return HELPER.registerBlock(variant.name() + "_table", TableBlockItem::new, value, TableBlock::new, variant);
    }

    private static Registered<Block> registerKitchenCounter(BlockVariant variant) {
        float value = ExchangeValues.round(0.33f * (2 * ExchangeValues.SLAB + 2 * variant.exchangeValue()));
        return HELPER.registerBlock(variant.name() + "_kitchen_counter", value, KitchenCounterBlock::new, variant);
    }

    private static Registered<Block> registerKitchenCupboard(BlockVariant variant) {
        float value = ExchangeValues.round(0.5f * (ExchangeValues.CHEST + 2 * 0.33f * (2 * ExchangeValues.SLAB + 2 * variant.exchangeValue())));
        return HELPER.registerBlock(variant.name() + "_kitchen_cupboard", value, KitchenCupboardBlock::new, variant);
    }

    private static Registered<Block> registerKitchenSink(BlockVariant variant) {
        float value = ExchangeValues.round(0.33f * (2 * ExchangeValues.SLAB + 2 * variant.exchangeValue()) + ExchangeValues.BUCKET);
        return HELPER.registerBlock(variant.name() + "_kitchen_sink", value, KitchenSinkBlock::new, variant);
    }

    private static Registered<Block> registerShelf(BlockVariant variant) {
        float value = ExchangeValues.round(0.33f * (3 * ExchangeValues.SLAB + 2 * ExchangeValues.STICK));
        return HELPER.registerBlock(variant.name() + "_shelf", value, ShelfBlock::new, variant);
    }

    private static Registered<Block> registerCoffeeTable(BlockVariant variant) {
        float value = ExchangeValues.round(0.5f * (2 * ExchangeValues.SLAB + 2 * ExchangeValues.STICK + ExchangeValues.GLASS_PANE));
        return HELPER.registerBlock(variant.name() + "_coffee_table", value, CoffeeTableBlock::new, variant);
    }

    private static Registered<Block> registerBench(BlockVariant variant) {
        float value = ExchangeValues.round(0.5f * 5 * ExchangeValues.SLAB);
        return HELPER.registerBlock(variant.name() + "_bench", value, BenchBlock::new, variant);
    }
}
