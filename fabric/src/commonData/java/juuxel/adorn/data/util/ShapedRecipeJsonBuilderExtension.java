package juuxel.adorn.data.util;

import net.minecraft.item.ItemStack;

import java.util.function.UnaryOperator;

public interface ShapedRecipeJsonBuilderExtension {
    void adorn_setOutputModifier(UnaryOperator<ItemStack> outputModifier);
}
