package juuxel.adorn.data.mixin;

import juuxel.adorn.AdornCommon;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CraftingRecipeJsonBuilder.class)
public interface CraftingRecipeJsonBuilderMixin {
    @ModifyVariable(method = "offerTo(Lnet/minecraft/data/recipe/RecipeExporter;Ljava/lang/String;)V", at = @At("HEAD"), argsOnly = true)
    private String addAdornNamespace(String recipePath) {
        if (recipePath.indexOf(Identifier.NAMESPACE_SEPARATOR) >= 0) {
            throw new UnsupportedOperationException("Don't call this method with a full namespace!");
        }

        return AdornCommon.NAMESPACE + Identifier.NAMESPACE_SEPARATOR + recipePath;
    }
}
