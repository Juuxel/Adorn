package juuxel.adorn.mixin;

import com.google.gson.JsonObject;
import juuxel.adorn.resources.TagExtensions;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Mixin(Tag.Builder.class)
public class TagBuilderMixin<T> {
    @Shadow @Final private Set<Tag.Entry<T>> entries;
    @Unique
    private static final Logger LOGGER = LogManager.getLogger("Adorn|TagBuilderMixin");

    @Inject(method = "fromJson", at = @At("HEAD"))
    private void onFromJson(Function<Identifier, Optional<T>> getter, JsonObject json, CallbackInfoReturnable<Tag.Builder<T>> info) {
        try {
            if (json.has("adorn_extensions")) {
                entries.addAll(TagExtensions.load(getter, JsonHelper.getObject(json, "adorn_extensions")));
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to load Adorn tag extensions in {}", json, e);
        }
    }
}
