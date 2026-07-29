package juuxel.adorn.lib.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public interface RegisteredItem<T extends Item> extends Registered.WithKey<Item, T>, ItemLike {
    default Item asItem() {
        return get();
    }
}
