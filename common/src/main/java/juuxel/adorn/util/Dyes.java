package juuxel.adorn.util;

import net.minecraft.world.item.DyeColor;

import java.util.Arrays;
import java.util.List;

public final class Dyes {
    /// All dyes in vanilla order. This list exists to replace [DyeColor#values()]
    /// which may have additional entries added by other mods.
    public static final List<DyeColor> ALL_DYES = List.of(
        DyeColor.WHITE,
        DyeColor.ORANGE,
        DyeColor.MAGENTA,
        DyeColor.LIGHT_BLUE,
        DyeColor.YELLOW,
        DyeColor.LIME,
        DyeColor.PINK,
        DyeColor.GRAY,
        DyeColor.LIGHT_GRAY,
        DyeColor.CYAN,
        DyeColor.PURPLE,
        DyeColor.BLUE,
        DyeColor.BROWN,
        DyeColor.GREEN,
        DyeColor.RED,
        DyeColor.BLACK
    );

    /// All dyes in creative inventory order.
    public static final List<DyeColor> DYES_IN_CREATIVE_INVENTORY_ORDER = List.of(
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

    public static void checkInDev() {
        if (!ALL_DYES.equals(Arrays.asList(DyeColor.values()))) {
            throw new AssertionError("Dyes.ALL_DYES doesn't match the value array of DyeColor!");
        }
    }
}
