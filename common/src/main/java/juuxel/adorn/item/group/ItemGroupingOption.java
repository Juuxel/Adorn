package juuxel.adorn.item.group;

import juuxel.adorn.util.Displayable;
import net.minecraft.network.chat.Component;

public enum ItemGroupingOption implements Displayable {
    BY_MATERIAL("by_material"),
    BY_SHAPE("by_shape");

    private final Component displayName;

    ItemGroupingOption(String id) {
        displayName = Component.translatable("gui.adorn.item_grouping_option." + id);
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }
}
