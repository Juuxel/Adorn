package juuxel.adorn.mixin;

import juuxel.adorn.util.TagWithState;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(targets = "net.minecraft.tag.RequiredTagList$TagWrapper")
abstract class RequiredTagListTagWrapperMixin<T> implements TagWithState {
    @Shadow
    @Nullable
    protected Tag<T> delegate;

    @Override
    public boolean adorn_isReady() {
        return delegate != null;
    }
}
