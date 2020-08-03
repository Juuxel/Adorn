package juuxel.adorn.mixin.access;

import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemTags.class)
public interface ItemTagsAccessor {
    @Invoker
    static Tag.Identified<Item> callRegister(String id) {
        throw new AssertionError("Untransformed Invoker body!");
    }
}
