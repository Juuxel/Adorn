package juuxel.adorn.item.group;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.block.variant.BlockKind;
import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.BlockVariantSets;
import juuxel.adorn.config.ConfigManager;
import juuxel.adorn.entity.ConeVariant;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.RegisteredMap;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import juuxel.adorn.platform.ItemGroupBridge;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class AdornItemGroups {
    public static final Registrar<ItemGroup> ITEM_GROUPS = RegistrarFactory.get().create(RegistryKeys.ITEM_GROUP);

    // Block kinds for each vanilla item group
    private static final List<BlockKind> BUILDING_KINDS = List.of(
        BlockKind.POST,
        BlockKind.PLATFORM,
        BlockKind.STEP
    );
    private static final List<BlockKind> FUNCTIONAL_KINDS = List.of(
        BlockKind.CHAIR,
        BlockKind.TABLE,
        BlockKind.DRAWER,
        BlockKind.KITCHEN_COUNTER,
        BlockKind.KITCHEN_CUPBOARD,
        BlockKind.KITCHEN_SINK,
        BlockKind.SHELF,
        BlockKind.COFFEE_TABLE,
        BlockKind.BENCH
    );

    public static final List<DyeColor> DYE_COLORS_IN_ORDER = List.of(
        DyeColor.WHITE,
        DyeColor.LIGHT_GRAY,
        DyeColor.GRAY,
        DyeColor.BLACK,
        DyeColor.BROWN,
        DyeColor.RED,
        DyeColor.ORANGE,
        DyeColor.YELLOW,
        DyeColor.LIME,
        DyeColor.GREEN,
        DyeColor.CYAN,
        DyeColor.LIGHT_BLUE,
        DyeColor.BLUE,
        DyeColor.PURPLE,
        DyeColor.MAGENTA,
        DyeColor.PINK
    );

    private static final String GROUP_ID = "items";
    public static final Registered<ItemGroup> GROUP = ITEM_GROUPS.register(GROUP_ID,
        () -> ItemGroupBridge.get().builder()
            .displayName(Text.translatable(Util.createTranslationKey("itemGroup", AdornCommon.id(GROUP_ID))))
            .icon(() -> new ItemStack(AdornBlocks.SOFAS.getEager(DyeColor.LIME)))
            .entries((displayContext, entries) -> {
                ItemGroupBuildContext context = new ItemGroupBuildContext() {
                    @Override
                    public void add(ItemConvertible item) {
                        entries.add(item);
                    }

                    @Override
                    public void add(ItemStack stack) {
                        entries.add(stack);
                    }

                    @Override
                    public RegistryWrapper.WrapperLookup getRegistries() {
                        return displayContext.lookup();
                    }
                };
                switch (ConfigManager.config().groupItems) {
                    case BY_MATERIAL -> addByKinds(context, Arrays.asList(BlockKind.values()));
                    case BY_SHAPE -> {
                        for (var kind : BlockKind.values()) {
                            for (var block : BlockVariantSets.get(kind)) {
                                context.add(block);
                            }
                        }

                        addColored(context, AdornBlocks.PAINTED_PLANKS);
                        addColored(context, AdornBlocks.PAINTED_WOOD_STAIRS);
                        addColored(context, AdornBlocks.PAINTED_WOOD_SLABS);
                        addColored(context, AdornBlocks.PAINTED_WOOD_FENCES);
                        addColored(context, AdornBlocks.PAINTED_WOOD_FENCE_GATES);
                        addColored(context, AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES);
                        addColored(context, AdornBlocks.PAINTED_WOOD_SLABS);
                    }
                }
                addColoredBlocks(context, false);
                addChimneys(context);
                addFences(context);
                addCopperPipes(context);
                addCrates(context);
                addMiscDecorations(context);
                context.add(AdornBlocks.TRADING_STATION);
                context.add(AdornBlocks.BREWER);
                addFoodAndDrink(context);
                addIngredients(context);
                addTools(context);
            })
            .build());

    public static void init() {
        if (ConfigManager.config().client.showItemsInStandardGroups) {
            addToVanillaItemGroups();
        }
    }

    private static void addToVanillaItemGroups() {
        var itemGroups = ItemGroupBridge.get();
        itemGroups.addItems(ItemGroups.BUILDING_BLOCKS, context -> {
            for (var variant : BlockVariantSets.allVariants()) {
                // Skip painted woods, they're added to the coloured blocks group instead.
                if (variant instanceof BlockVariant.PaintedWood) continue;

                var after = findLastBuildingBlockEntry(variant);

                if (after != null) {
                    List<ItemConvertible> items = new ArrayList<>();
                    for (var kind : BUILDING_KINDS) {
                        var block = BlockVariantSets.get(kind, variant);
                        if (block != null) items.add(block.get());
                    }
                    context.addAfter(after, items);
                } else {
                    addByKinds(context, variant, BUILDING_KINDS);
                }
            }

            context.addAfter(Items.CUT_COPPER_SLAB, AdornBlocks.COPPER_PIPE);
            context.addAfter(Items.EXPOSED_CUT_COPPER_SLAB, AdornBlocks.EXPOSED_COPPER_PIPE);
            context.addAfter(Items.WEATHERED_CUT_COPPER_SLAB, AdornBlocks.WEATHERED_COPPER_PIPE);
            context.addAfter(Items.OXIDIZED_CUT_COPPER_SLAB, AdornBlocks.OXIDIZED_COPPER_PIPE);
            context.addAfter(Items.WAXED_CUT_COPPER_SLAB, AdornBlocks.WAXED_COPPER_PIPE);
            context.addAfter(Items.WAXED_EXPOSED_CUT_COPPER_SLAB, AdornBlocks.WAXED_EXPOSED_COPPER_PIPE);
            context.addAfter(Items.WAXED_WEATHERED_CUT_COPPER_SLAB, AdornBlocks.WAXED_WEATHERED_COPPER_PIPE);
            context.addAfter(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB, AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE);
        });
        itemGroups.addItems(ItemGroups.COLORED_BLOCKS, context -> addColoredBlocks(context, true));
        itemGroups.addItems(ItemGroups.FUNCTIONAL, context -> {
            addByKinds(context, FUNCTIONAL_KINDS);
            addColoredBlocks(context, false);
            addChimneys(context);
            addFences(context);
            addCrates(context);
            addMiscDecorations(context);
            context.add(AdornBlocks.TRADING_STATION);
            context.add(AdornBlocks.BREWER);
        });
        itemGroups.addItems(ItemGroups.FOOD_AND_DRINK, AdornItemGroups::addFoodAndDrink);
        itemGroups.addItems(ItemGroups.INGREDIENTS, AdornItemGroups::addIngredients);
        itemGroups.addItems(ItemGroups.TOOLS, AdornItemGroups::addTools);
    }

    private static void addByKinds(ItemGroupBuildContext context, List<BlockKind> kinds) {
        var hasAllKinds = kinds.equals(Arrays.asList(BlockKind.values()));

        for (var variant : BlockVariantSets.allVariants()) {
            if (hasAllKinds && variant instanceof BlockVariant.PaintedWood(var color)) {
                context.add(AdornBlocks.PAINTED_PLANKS.get(color));
                context.add(AdornBlocks.PAINTED_WOOD_STAIRS.get(color));
                context.add(AdornBlocks.PAINTED_WOOD_SLABS.get(color));
                context.add(AdornBlocks.PAINTED_WOOD_FENCES.get(color));
                context.add(AdornBlocks.PAINTED_WOOD_FENCE_GATES.get(color));
                context.add(AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.get(color));
                context.add(AdornBlocks.PAINTED_WOOD_BUTTONS.get(color));
            }

            addByKinds(context, variant, kinds);
        }
    }

    private static void addByKinds(ItemGroupBuildContext context, BlockVariant variant, List<BlockKind> kinds) {
        for (var kind : kinds) {
            var block = BlockVariantSets.get(kind, variant);
            if (block != null) context.add(block);
        }
    }

    private static void addColoredBlocks(ItemGroupBuildContext context, boolean includeWood) {
        addColored(context, AdornBlocks.SOFAS);
        addColored(context, AdornBlocks.TABLE_LAMPS);
        if (includeWood) {
            addColored(context, AdornBlocks.PAINTED_PLANKS);
            addColored(context, AdornBlocks.PAINTED_WOOD_STAIRS);
            addColored(context, AdornBlocks.PAINTED_WOOD_SLABS);
            addColored(context, AdornBlocks.PAINTED_WOOD_FENCES);
            addColored(context, AdornBlocks.PAINTED_WOOD_FENCE_GATES);
            addColored(context, AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES);
            addColored(context, AdornBlocks.PAINTED_WOOD_BUTTONS);

            for (BlockKind kind : BlockKind.values()) {
                for (DyeColor color : DYE_COLORS_IN_ORDER) {
                    var item = BlockVariantSets.get(kind, BlockVariant.PAINTED_WOODS.get(color));
                    if (item != null) context.add(item);
                }
            }
        }
        context.add(AdornBlocks.CANDLELIT_LANTERN);
        addColored(context, AdornBlocks.DYED_CANDLELIT_LANTERNS);
        addCones(context);
    }

    private static void addCones(ItemGroupBuildContext context) {
        for (ConeVariant variant : ConeVariant.values()) {
            context.add(new ItemStack(AdornItems.CONES.getEager(variant)));
        }
    }

    private static void addColored(ItemGroupBuildContext context, RegisteredMap<DyeColor, ? extends ItemConvertible> items) {
        for (DyeColor color : DYE_COLORS_IN_ORDER) {
            context.add(items.get(color));
        }
    }

    private static void addCrates(ItemGroupBuildContext context) {
        context.add(AdornBlocks.CRATE);
        context.add(AdornBlocks.APPLE_CRATE);
        context.add(AdornBlocks.WHEAT_CRATE);
        context.add(AdornBlocks.CARROT_CRATE);
        context.add(AdornBlocks.POTATO_CRATE);
        context.add(AdornBlocks.MELON_CRATE);
        context.add(AdornBlocks.WHEAT_SEED_CRATE);
        context.add(AdornBlocks.MELON_SEED_CRATE);
        context.add(AdornBlocks.PUMPKIN_SEED_CRATE);
        context.add(AdornBlocks.BEETROOT_CRATE);
        context.add(AdornBlocks.BEETROOT_SEED_CRATE);
        context.add(AdornBlocks.SWEET_BERRY_CRATE);
        context.add(AdornBlocks.COCOA_BEAN_CRATE);
        context.add(AdornBlocks.NETHER_WART_CRATE);
        context.add(AdornBlocks.SUGAR_CANE_CRATE);
        context.add(AdornBlocks.EGG_CRATE);
        context.add(AdornBlocks.HONEYCOMB_CRATE);
        context.add(AdornBlocks.LIL_TATER_CRATE);
    }

    private static void addChimneys(ItemGroupBuildContext context) {
        context.add(AdornBlocks.BRICK_CHIMNEY);
        context.add(AdornBlocks.STONE_BRICK_CHIMNEY);
        context.add(AdornBlocks.NETHER_BRICK_CHIMNEY);
        context.add(AdornBlocks.RED_NETHER_BRICK_CHIMNEY);
        context.add(AdornBlocks.COBBLESTONE_CHIMNEY);
        context.add(AdornBlocks.PRISMARINE_CHIMNEY);
        context.add(AdornBlocks.MAGMATIC_PRISMARINE_CHIMNEY);
        context.add(AdornBlocks.SOULFUL_PRISMARINE_CHIMNEY);
    }

    private static void addFences(ItemGroupBuildContext context) {
        context.add(AdornBlocks.PICKET_FENCE);
        context.add(AdornBlocks.CHAIN_LINK_FENCE);
    }

    private static void addCopperPipes(ItemGroupBuildContext context) {
        context.add(AdornBlocks.COPPER_PIPE);
        context.add(AdornBlocks.EXPOSED_COPPER_PIPE);
        context.add(AdornBlocks.WEATHERED_COPPER_PIPE);
        context.add(AdornBlocks.OXIDIZED_COPPER_PIPE);
        context.add(AdornBlocks.WAXED_COPPER_PIPE);
        context.add(AdornBlocks.WAXED_EXPOSED_COPPER_PIPE);
        context.add(AdornBlocks.WAXED_WEATHERED_COPPER_PIPE);
        context.add(AdornBlocks.WAXED_OXIDIZED_COPPER_PIPE);
    }

    private static void addFoodAndDrink(ItemGroupBuildContext context) {
        context.add(AdornItems.MUG);
        context.add(AdornItems.HOT_CHOCOLATE);
        context.add(AdornItems.SWEET_BERRY_JUICE);
        context.add(AdornItems.GLOW_BERRY_TEA);
        context.add(AdornItems.NETHER_WART_COFFEE);
    }

    private static void addIngredients(ItemGroupBuildContext context) {
        if (context instanceof ItemGroupModifyContext modifyContext) {
            modifyContext.addAfter(Items.STICK, AdornItems.STONE_ROD);
            modifyContext.addAfter(Items.IRON_NUGGET, AdornItems.COPPER_NUGGET);
        } else {
            context.add(AdornItems.STONE_ROD);
            context.add(AdornItems.COPPER_NUGGET);
        }
    }

    private static void addTools(ItemGroupBuildContext context) {
        context.add(AdornItems.WATERING_CAN);
        context.add(AdornItems.GUIDE_BOOK);
        context.add(AdornItems.TRADERS_MANUAL);
    }

    private static void addMiscDecorations(ItemGroupBuildContext context) {
        context.add(AdornItems.STONE_TORCH);
        context.add(AdornBlocks.STONE_LADDER);
    }

    private static List<ItemConvertible> getPaintedWood() {
        List<ItemConvertible> items = new ArrayList<>();

        for (DyeColor color : DYE_COLORS_IN_ORDER) {
            items.add(AdornBlocks.PAINTED_PLANKS.getEager(color));
            items.add(AdornBlocks.PAINTED_WOOD_SLABS.getEager(color));
            items.add(AdornBlocks.PAINTED_WOOD_STAIRS.getEager(color));
            items.add(AdornBlocks.PAINTED_WOOD_FENCES.getEager(color));
            items.add(AdornBlocks.PAINTED_WOOD_FENCE_GATES.getEager(color));
            items.add(AdornBlocks.PAINTED_WOOD_PRESSURE_PLATES.getEager(color));
            items.add(AdornBlocks.PAINTED_WOOD_BUTTONS.getEager(color));
        }

        return items;
    }

    private static @Nullable Block findLastBuildingBlockEntry(BlockVariant variant) {
        // Buttons are the last entry in "building blocks" for each material currently,
        // then walls and finally slabs
        return findBaseBlock(variant, "button")
            .or(() -> findBaseBlock(variant, "wall"))
            .or(() -> findBaseBlock(variant, "slab"))
            .or(() -> findBaseBlock(variant, null))
            .orElse(null);
    }

    private static Optional<Block> findBaseBlock(BlockVariant variant, @Nullable String suffix) {
        var variantId = variant.nameAsIdentifier();
        var buttonId = suffix != null ? variantId.withSuffixedPath("_" + suffix) : variantId;

        if (Registries.BLOCK.containsId(buttonId)) {
            return Optional.of(Registries.BLOCK.get(buttonId));
        }

        return Optional.empty();
    }
}
