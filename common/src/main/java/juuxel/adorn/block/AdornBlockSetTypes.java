package juuxel.adorn.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import juuxel.adorn.AdornCommon;
import net.minecraft.block.BlockSetType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;

public final class AdornBlockSetTypes {
    public static final BiMap<DyeColor, BlockSetType> PAINTED_WOODS = Util.make(EnumHashBiMap.create(DyeColor.class), map -> {
        for (DyeColor color : DyeColor.values()) {
            map.put(color, register(color.getId() + "_wood"));
        }
    });

    public static void init() {
    }

    private static BlockSetType register(String id) {
        return BlockSetType.register(new BlockSetType(AdornCommon.NAMESPACE + ':' + id));
    }
}
