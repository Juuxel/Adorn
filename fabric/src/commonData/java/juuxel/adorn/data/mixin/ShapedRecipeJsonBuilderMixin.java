package juuxel.adorn.data.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import juuxel.adorn.data.util.ShapedRecipeJsonBuilderExtension;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.UnaryOperator;

@Mixin(ShapedRecipeJsonBuilder.class)
abstract class ShapedRecipeJsonBuilderMixin implements ShapedRecipeJsonBuilderExtension {
    @Unique
    private @Nullable UnaryOperator<ItemStack> outputModifier;

    @Override
    public void adorn_setOutputModifier(UnaryOperator<ItemStack> outputModifier) {
        this.outputModifier = outputModifier;
    }

    @ModifyExpressionValue(method = "offerTo", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack"))
    private ItemStack applyModifiers(ItemStack stack) {
        return outputModifier != null ? outputModifier.apply(stack) : stack;
    }
}
