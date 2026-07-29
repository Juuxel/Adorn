package juuxel.adorn.lib.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public interface RegisteredBlock<T extends Block> extends Registered.WithKey<Block, T>, ItemLike {
    default Item asItem() {
        return get().asItem();
    }
}
