package juuxel.adorn.item.group;

import net.minecraft.world.level.ItemLike;

import java.util.List;

public interface ItemGroupModifyContext extends ItemGroupBuildContext {
    void addBefore(ItemLike before, List<? extends ItemLike> items);

    default void addBefore(ItemLike before, ItemLike item) {
        addBefore(before, List.of(item));
    }

    void addAfter(ItemLike after, List<? extends ItemLike> items);

    default void addAfter(ItemLike after, ItemLike item) {
        addAfter(after, List.of(item));
    }
}
