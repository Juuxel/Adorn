package juuxel.adorn.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumHashBiMap;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.util.Dyes;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.util.Util;

public final class AdornBlockSetTypes {
    public static final BiMap<DyeColor, BlockSetType> PAINTED_WOODS = Util.make(EnumHashBiMap.create(DyeColor.class), map -> {
        for (DyeColor color : Dyes.ALL_DYES) {
            map.put(color, register(color.getName() + "_wood"));
        }
    });

    public static void init() {
    }

    private static BlockSetType register(String id) {
        return BlockSetType.register(new BlockSetType(AdornCommon.NAMESPACE + ':' + id));
    }
}
