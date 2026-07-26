package juuxel.adorn.data.mixin;

import juuxel.adorn.AdornCommon;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RecipeBuilder.class)
public interface RecipeBuilderMixin {
    @ModifyVariable(method = "save(Lnet/minecraft/data/recipes/RecipeOutput;Ljava/lang/String;)V", at = @At("HEAD"), argsOnly = true)
    private String addAdornNamespace(String recipePath) {
        if (recipePath.indexOf(Identifier.NAMESPACE_SEPARATOR) >= 0) {
            throw new UnsupportedOperationException("Don't call this method with a full namespace!");
        }

        return AdornCommon.NAMESPACE + Identifier.NAMESPACE_SEPARATOR + recipePath;
    }
}
