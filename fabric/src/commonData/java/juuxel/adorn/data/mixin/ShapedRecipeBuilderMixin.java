package juuxel.adorn.data.mixin;

import juuxel.adorn.data.util.ShapedRecipeJsonBuilderExtension;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.UnaryOperator;

@Mixin(ShapedRecipeBuilder.class)
abstract class ShapedRecipeBuilderMixin implements ShapedRecipeJsonBuilderExtension {
    @Unique
    private @Nullable UnaryOperator<ItemStackTemplate> outputModifier;

    @Override
    public void adorn_setOutputModifier(UnaryOperator<ItemStackTemplate> outputModifier) {
        this.outputModifier = outputModifier;
    }

    @ModifyArg(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/ShapedRecipe;<init>(Lnet/minecraft/world/item/crafting/Recipe$CommonInfo;Lnet/minecraft/world/item/crafting/CraftingRecipe$CraftingBookInfo;Lnet/minecraft/world/item/crafting/ShapedRecipePattern;Lnet/minecraft/world/item/ItemStackTemplate;)V"))
    private ItemStackTemplate applyModifiers(ItemStackTemplate template) {
        return outputModifier != null ? outputModifier.apply(template) : template;
    }
}
