package juuxel.adorn.platform.forge.mixin.client;

import com.google.gson.JsonObject;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// Removes log spam from mod compat models with unknown textures by making the models empty.
@Mixin(JsonUnbakedModel.Deserializer.class)
abstract class JsonUnbakedModelDeserializerMixin {
    @ModifyVariable(
        method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/render/model/json/JsonUnbakedModel;",
        at = @At(value = "INVOKE_ASSIGN", target = "Lcom/google/gson/JsonElement;getAsJsonObject()Lcom/google/gson/JsonObject;")
    )
    private JsonObject deserializeConditionally(JsonObject json) {
        if (json.has("adorn:conditions")) {
            var conditionResult = ICondition.LIST_CODEC.parse(JsonOps.INSTANCE, json.get("adorn:conditions"));
            if (conditionResult instanceof DataResult.Success(var conditions, var lifecycle)) {
                for (ICondition condition : conditions) {
                    // Replace contents with an empty object if the conditions fail
                    if (!condition.test(ICondition.IContext.EMPTY)) return new JsonObject();
                }
            }
        }

        return json;
    }
}
