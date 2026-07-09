package juuxel.adorn.compat;

import juuxel.adorn.block.variant.BlockVariant;
import juuxel.adorn.block.variant.CompatBlockVariantSet;

import java.util.List;

public final class BwgCompat extends CompatBlockVariantSet {
    @Override
    protected String getModId() {
        return "biomeswevegone";
    }

    @Override
    public List<BlockVariant> getWoodVariants() {
        return createVariants(
            BlockVariant.Wood::new,
            "aspen",
            "baobab",
            "blue_enchanted",
            "cika",
            "cypress",
            "ebony",
            "fir",
            "florus",
            "green_enchanted",
            "holly",
            "ironwood",
            "jacaranda",
            "mahogany",
            "maple",
            "palm",
            "pine",
            "rainbow_eucalyptus",
            "redwood",
            "sakura",
            "skyris",
            "white_mangrove",
            "willow",
            "witch_hazel",
            "zelkova"
        );
    }

    @Override
    public List<BlockVariant> getStoneVariants() {
        return createVariants(
            BlockVariant.Stone::new,
            "black_sandstone",
            "blue_sandstone",
            "cut_black_sandstone",
            "cut_blue_sandstone",
            "cut_pink_sandstone",
            "cut_purple_sandstone",
            "cut_white_sandstone",
            "cut_windswept_sandstone",
            "chiseled_dacite_brick",
            "chiseled_red_rock_brick",
            "chiseled_white_dacite_brick",
            "cracked_dacite_brick",
            "cracked_red_rock_brick",
            "cracked_white_dacite_brick",
            "dacite_brick",
            "dacite_cobblestone",
            "dacite_tile",
            "dacite",
            "mossy_dacite_brick",
            "mossy_red_rock_brick",
            "mossy_stone",
            "mossy_white_dacite_brick",
            "pale_mud_bricks",
            "pink_sandstone",
            "polished_red_rock",
            "purple_sandstone",
            "red_rock_brick",
            "red_rock_tile",
            "red_rock",
            "rocky_stone",
            "smooth_black_sandstone",
            "smooth_blue_sandstone",
            "smooth_pink_sandstone",
            "smooth_purple_sandstone",
            "smooth_white_sandstone",
            "smooth_windswept_sandstone",
            "white_dacite_brick",
            "white_dacite_cobblestone",
            "white_dacite_tile",
            "white_dacite",
            "white_sandstone",
            "windswept_sandstone"
        );
    }
}
