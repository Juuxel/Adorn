package juuxel.adorn.platform.forge.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.loot.LootDataType;
import net.minecraft.loot.LootTable;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * This mixin removes all loot tables whose "adorn:conditions" don't match the loading environment.
 */
@Mixin(LootDataType.class)
abstract class LootDataTypeMixin<T> {
    @Shadow
    @Final
    public static LootDataType<LootTable> LOOT_TABLES;

    @Inject(method = "deserialize", at = @At("HEAD"), cancellable = true)
    private void adorn_onApply(Identifier id, JsonElement json, ResourceManager resourceManager, CallbackInfoReturnable<Optional<T>> info) {
        if ((Object) this != LOOT_TABLES) return;
        JsonObject obj = json.getAsJsonObject();

        // Let's just use IContext.EMPTY here since we don't need the tag conditions.
        // Mixing into DataPackContents is too much work ;)
        if (!CraftingHelper.processConditions(obj, "adorn:conditions", ICondition.IContext.EMPTY)) {
            info.setReturnValue(Optional.empty());
        }
    }
}
