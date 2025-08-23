package juuxel.adorn.recipe;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.fluid.FluidIngredient;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class BrewingRecipeJsonBuilder {
    private final RegistryEntryLookup<Item> itemLookup;
    private final ItemStack result;
    private @Nullable Ingredient firstIngredient;
    private @Nullable Ingredient secondIngredient;
    private @Nullable FluidIngredient fluid;

    private BrewingRecipeJsonBuilder(RegistryEntryLookup<Item> itemLookup, ItemStack result) {
        this.itemLookup = itemLookup;
        this.result = result;
    }

    public static BrewingRecipeJsonBuilder create(RegistryEntryLookup<Item> itemLookup, ItemConvertible item) {
        return create(itemLookup, item, 1);
    }

    public static BrewingRecipeJsonBuilder create(RegistryEntryLookup<Item> itemLookup, ItemConvertible item, int count) {
        return new BrewingRecipeJsonBuilder(itemLookup, new ItemStack(item, count));
    }

    public BrewingRecipeJsonBuilder first(ItemConvertible item) {
        firstIngredient = Ingredient.ofItem(item);
        return this;
    }

    public BrewingRecipeJsonBuilder first(TagKey<Item> tag) {
        firstIngredient = Ingredient.ofTag(itemLookup.getOrThrow(tag));
        return this;
    }

    public BrewingRecipeJsonBuilder second(ItemConvertible item) {
        secondIngredient = Ingredient.ofItem(item);
        return this;
    }

    public BrewingRecipeJsonBuilder second(TagKey<Item> tag) {
        secondIngredient = Ingredient.ofTag(itemLookup.getOrThrow(tag));
        return this;
    }

    public BrewingRecipeJsonBuilder fluid(FluidIngredient fluid) {
        this.fluid = fluid;
        return this;
    }

    public void offerTo(RecipeExporter exporter) {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(result.getItem()).getPath());
    }

    public void offerTo(RecipeExporter exporter, String recipeName) {
        Objects.requireNonNull(firstIngredient, "First ingredient of brewing recipe not set");
        var key = RegistryKey.of(RegistryKeys.RECIPE, AdornCommon.id("brewing/" + recipeName));
        BrewingRecipe recipe = fluid != null
            ? new FluidBrewingRecipe(firstIngredient, Optional.ofNullable(secondIngredient), fluid, result)
            : new ItemBrewingRecipe(firstIngredient, Optional.ofNullable(secondIngredient), result);
        exporter.accept(key, recipe, null);
    }
}
