package juuxel.adorn.data.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import juuxel.adorn.data.AdornModelGenerator;
import net.minecraft.data.DataProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DataProvider.class)
interface DataProviderMixin {
    @ModifyArg(method = "lambda$saveAll$0", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;encodeStart(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;"))
    private static DynamicOps<JsonElement> useRegistryOps(DynamicOps<JsonElement> ops) {
        if (AdornModelGenerator.REGISTRIES.isBound()) {
            return AdornModelGenerator.REGISTRIES.get().createSerializationContext(ops);
        }

        return ops;
    }
}
