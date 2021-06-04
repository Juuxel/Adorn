package juuxel.adorn.platform.fabric;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.item.AdornBookItem;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Rarity;

public final class ItemBridgeImpl {
    public static ItemGroup createAdornItemGroup() {
        return FabricItemGroupBuilder.build(
            AdornCommon.id("items"),
            () -> new ItemStack(AdornBlocks.INSTANCE.getSOFAS().get(DyeColor.LIME))
        );
    }

    public static Item createGuideBook() {
        return new AdornBookItem(
            AdornCommon.id("guide"), new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.UNCOMMON)
        );
    }

    public static Item createTradersManual() {
        return new AdornBookItem(AdornCommon.id("traders_manual"), new Item.Settings().group(ItemGroup.MISC));
    }
}
