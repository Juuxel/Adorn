package juuxel.adorn.platform;

import juuxel.adorn.item.group.ItemGroupModifyContext;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.ResourceKey;

import java.util.function.Consumer;

@InlineServices
public interface ItemGroupBridge {
    CreativeModeTab.Builder builder();
    void addItems(ResourceKey<CreativeModeTab> group, Consumer<ItemGroupModifyContext> configurator);

    @InlineServices.Getter
    static ItemGroupBridge get() {
        return Services.load(ItemGroupBridge.class);
    }
}
