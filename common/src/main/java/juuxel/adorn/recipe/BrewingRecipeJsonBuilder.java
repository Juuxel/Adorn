package juuxel.adorn.recipe;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.item.AdornItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class BrewingRecipeJsonBuilder {
    private final HolderGetter<Item> itemLookup;
    private final ItemStackTemplate result;
    private Ingredient input = Ingredient.of(AdornItems.MUG.get());
    private @Nullable Ingredient firstIngredient;
    private @Nullable Ingredient secondIngredient;
    private @Nullable FluidIngredient fluid;
    private final Map<String, Criterion<?>> criteria = new HashMap<>();

    private BrewingRecipeJsonBuilder(HolderGetter<Item> itemLookup, ItemStackTemplate result) {
        this.itemLookup = itemLookup;
        this.result = result;
    }

    public static BrewingRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike item) {
        return create(itemLookup, item, 1);
    }

    public static BrewingRecipeJsonBuilder create(HolderGetter<Item> itemLookup, ItemLike item, int count) {
        return new BrewingRecipeJsonBuilder(itemLookup, new ItemStackTemplate(item.asItem(), count));
    }

    public BrewingRecipeJsonBuilder input(ItemLike item) {
        input = Ingredient.of(item);
        return this;
    }

    public BrewingRecipeJsonBuilder input(TagKey<Item> tag) {
        input = Ingredient.of(itemLookup.getOrThrow(tag));
        return this;
    }

    public BrewingRecipeJsonBuilder first(ItemLike item) {
        firstIngredient = Ingredient.of(item);
        return this;
    }

    public BrewingRecipeJsonBuilder first(TagKey<Item> tag) {
        firstIngredient = Ingredient.of(itemLookup.getOrThrow(tag));
        return this;
    }

    public BrewingRecipeJsonBuilder second(ItemLike item) {
        secondIngredient = Ingredient.of(item);
        return this;
    }

    public BrewingRecipeJsonBuilder second(TagKey<Item> tag) {
        secondIngredient = Ingredient.of(itemLookup.getOrThrow(tag));
        return this;
    }

    public BrewingRecipeJsonBuilder fluid(FluidIngredient fluid) {
        this.fluid = fluid;
        return this;
    }

    public BrewingRecipeJsonBuilder criterion(String name, Criterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    public void offerTo(RecipeOutput exporter) {
        offerTo(exporter, RecipeBuilder.getDefaultRecipeId(result).identifier().getPath());
    }

    public void offerTo(RecipeOutput exporter, String recipeName) {
        var key = ResourceKey.create(Registries.RECIPE, AdornCommon.id("brewing/" + recipeName));
        validate(key);
        Advancement.Builder advancementBuilder = exporter.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
            .rewards(AdvancementRewards.Builder.recipe(key))
            .requirements(AdvancementRequirements.Strategy.OR);
        var commonInfo = RecipeBuilder.createCraftingCommonInfo(true);
        criteria.forEach(advancementBuilder::addCriterion);
        BrewingRecipe recipe = fluid != null
            ? new FluidBrewingRecipe(commonInfo, input, firstIngredient, Optional.ofNullable(secondIngredient), fluid, result)
            : new ItemBrewingRecipe(commonInfo, input, firstIngredient, Optional.ofNullable(secondIngredient), result);
        exporter.accept(key, recipe, advancementBuilder.build(key.identifier().withPrefix("recipes/")));
    }

    private void validate(ResourceKey<Recipe<?>> key) {
        if (firstIngredient == null) {
            throw new NullPointerException("First ingredient of brewing recipe " + key + " not set");
        }

        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + key.identifier());
        }
    }
}
