package juuxel.adorn.mixin;

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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This mixin removes all loot tables whose "adorn:conditions" don't match the loading environment.
 */
@Mixin(LootManager.class)
abstract class LootManagerMixin {
    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"))
    private void adorn_onApply(Map<Identifier, JsonElement> lootTableJsons, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        Set<Identifier> keysToRemove = null;

        for (Map.Entry<Identifier, JsonElement> entry : lootTableJsons.entrySet()) {
            JsonObject json = entry.getValue().getAsJsonObject();

            if (!CraftingHelper.processConditions(json, "adorn:conditions")) {
                if (keysToRemove == null) keysToRemove = new HashSet<>();
                keysToRemove.add(entry.getKey());
            }
        }

        if (keysToRemove != null) {
            for (Identifier key : keysToRemove) {
                lootTableJsons.remove(key);
            }
        }
    }
}
