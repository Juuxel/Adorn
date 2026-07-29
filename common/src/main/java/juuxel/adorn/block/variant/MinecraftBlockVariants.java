package juuxel.adorn.block.variant;

import juuxel.adorn.util.Dyes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class MinecraftBlockVariants implements BlockVariantSet {
    @Override
    public List<BlockVariant> getWoodVariants() {
        List<BlockVariant> result = new ArrayList<>();
        result.add(BlockVariant.OAK);
        result.add(BlockVariant.SPRUCE);
        result.add(BlockVariant.BIRCH);
        result.add(BlockVariant.JUNGLE);
        result.add(BlockVariant.ACACIA);
        result.add(BlockVariant.DARK_OAK);
        result.add(BlockVariant.MANGROVE);
        result.add(BlockVariant.CHERRY);
        result.add(BlockVariant.PALE_OAK);
        result.add(BlockVariant.BAMBOO);
        result.add(BlockVariant.CRIMSON);
        result.add(BlockVariant.WARPED);
        result.addAll(
            BlockVariant.PAINTED_WOODS
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(
                    Comparator.comparingInt(Dyes.DYES_IN_CREATIVE_INVENTORY_ORDER::indexOf)
                ))
                .map(Map.Entry::getValue)
                .toList()
        );
        return result;
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return List.of(
            BlockVariant.STONE,
            BlockVariant.COBBLESTONE,
            BlockVariant.SANDSTONE,
            BlockVariant.DIORITE,
            BlockVariant.ANDESITE,
            BlockVariant.GRANITE,
            BlockVariant.BRICK,
            BlockVariant.STONE_BRICK,
            BlockVariant.RED_SANDSTONE,
            BlockVariant.NETHER_BRICK,
            BlockVariant.BASALT,
            BlockVariant.BLACKSTONE,
            BlockVariant.RED_NETHER_BRICK,
            BlockVariant.PRISMARINE,
            BlockVariant.QUARTZ,
            BlockVariant.END_STONE_BRICK,
            BlockVariant.PURPUR,
            BlockVariant.POLISHED_BLACKSTONE,
            BlockVariant.POLISHED_BLACKSTONE_BRICK,
            BlockVariant.POLISHED_DIORITE,
            BlockVariant.POLISHED_ANDESITE,
            BlockVariant.POLISHED_GRANITE,
            BlockVariant.PRISMARINE_BRICK,
            BlockVariant.DARK_PRISMARINE,
            BlockVariant.CUT_SANDSTONE,
            BlockVariant.SMOOTH_SANDSTONE,
            BlockVariant.CUT_RED_SANDSTONE,
            BlockVariant.SMOOTH_RED_SANDSTONE,
            BlockVariant.SMOOTH_STONE,
            BlockVariant.MOSSY_COBBLESTONE,
            BlockVariant.MOSSY_STONE_BRICK,
            BlockVariant.DEEPSLATE,
            BlockVariant.COBBLED_DEEPSLATE,
            BlockVariant.POLISHED_DEEPSLATE,
            BlockVariant.DEEPSLATE_BRICK,
            BlockVariant.DEEPSLATE_TILE,
            BlockVariant.TUFF,
            BlockVariant.POLISHED_TUFF,
            BlockVariant.TUFF_BRICK,
            BlockVariant.MUD_BRICK,
            BlockVariant.CINNABAR,
            BlockVariant.CINNABAR_BRICK,
            BlockVariant.POLISHED_CINNABAR,
            BlockVariant.SULFUR,
            BlockVariant.SULFUR_BRICK,
            BlockVariant.POLISHED_SULFUR,
            BlockVariant.RESIN_BRICK
        );
    }

    @Override
    public void addVariants(CustomVariantConsumer consumer) {
        consumer.add(BlockVariant.IRON, List.of(BlockKind.SHELF));
    }

    @Override
    public void sortVariants(Sorter sorter) {
        sorter.moveAfter(BlockVariant.IRON, getWoodVariants().getLast());
    }
}
