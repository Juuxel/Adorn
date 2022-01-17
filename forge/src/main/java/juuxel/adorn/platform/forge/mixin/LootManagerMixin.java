package juuxel.adorn.platform.forge.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.loot.LootManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

/**
 * This mixin removes all loot tables whose "adorn:conditions" don't match the loading environment.
 */
@Mixin(LootManager.class)
abstract class LootManagerMixin {
    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"))
    private void adorn_onApply(Map<Identifier, JsonElement> lootTableJsons, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        Iterator<JsonElement> iter = lootTableJsons.values().iterator();
        while (iter.hasNext()) {
            JsonObject json = iter.next().getAsJsonObject();

            if (!CraftingHelper.processConditions(json, "adorn:conditions")) {
                iter.remove();
            }
        }
    }
}
