package juuxel.adorn.block;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.util.Dyes;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.Map;

public final class AdornWoodTypes {
    public static final Map<DyeColor, WoodType> PAINTED_WOODS = Util.make(new EnumMap<>(DyeColor.class), map -> {
        for (DyeColor color : Dyes.ALL_DYES) {
            map.put(color, register(color.getName() + "_wood", AdornBlockSetTypes.PAINTED_WOODS.get(color)));
        }
    });

    public static void init() {
    }

    private static WoodType register(String id, BlockSetType blockSetType) {
        return WoodType.register(new WoodType(AdornCommon.NAMESPACE + ':' + id, blockSetType));
    }
}
