package juuxel.adorn.recipe;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.item.AdornItems;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class BrewingRecipeJsonBuilder {
    private final RegistryEntryLookup<Item> itemLookup;
    private final ItemStack result;
    private Ingredient input = Ingredient.ofItem(AdornItems.MUG.get());
    private @Nullable Ingredient firstIngredient;
    private @Nullable Ingredient secondIngredient;
    private @Nullable FluidIngredient fluid;
    private final Map<String, AdvancementCriterion<?>> criteria = new HashMap<>();

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

    public BrewingRecipeJsonBuilder input(ItemConvertible item) {
        input = Ingredient.ofItem(item);
        return this;
    }

    public BrewingRecipeJsonBuilder input(TagKey<Item> tag) {
        input = Ingredient.ofTag(itemLookup.getOrThrow(tag));
        return this;
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

    public BrewingRecipeJsonBuilder criterion(String name, AdvancementCriterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    public void offerTo(RecipeExporter exporter) {
        offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(result.getItem()).getPath());
    }

    public void offerTo(RecipeExporter exporter, String recipeName) {
        var key = RegistryKey.of(RegistryKeys.RECIPE, AdornCommon.id("brewing/" + recipeName));
        validate(key);
        Advancement.Builder advancementBuilder = exporter.getAdvancementBuilder()
            .criterion("has_the_recipe", RecipeUnlockedCriterion.create(key))
            .rewards(AdvancementRewards.Builder.recipe(key))
            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        criteria.forEach(advancementBuilder::criterion);
        BrewingRecipe recipe = fluid != null
            ? new FluidBrewingRecipe(input, firstIngredient, Optional.ofNullable(secondIngredient), fluid, result)
            : new ItemBrewingRecipe(input, firstIngredient, Optional.ofNullable(secondIngredient), result);
        exporter.accept(key, recipe, advancementBuilder.build(key.getValue().withPrefixedPath("recipes/")));
    }

    private void validate(RegistryKey<Recipe<?>> key) {
        if (firstIngredient == null) {
            throw new NullPointerException("First ingredient of brewing recipe " + key + " not set");
        }

        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + key.getValue());
        }
    }
}
