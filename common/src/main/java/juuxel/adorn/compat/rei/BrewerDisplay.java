package juuxel.adorn.compat.rei;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.fluid.FluidStack;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.platform.FluidBridge;
import juuxel.adorn.recipe.FluidBrewingRecipe;
import juuxel.adorn.recipe.ItemBrewingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record BrewerDisplay(
    EntryIngredient input,
    EntryIngredient first,
    EntryIngredient second,
    EntryIngredient fluid,
    EntryStack<?> result,
    Optional<Identifier> id
) implements Display {
    public static final MapCodec<BrewerDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        EntryIngredient.codec().fieldOf("input").forGetter(BrewerDisplay::input),
        EntryIngredient.codec().fieldOf("first_ingredient").forGetter(BrewerDisplay::first),
        EntryIngredient.codec().fieldOf("second_ingredient").forGetter(BrewerDisplay::second),
        EntryIngredient.codec().fieldOf("fluid").forGetter(BrewerDisplay::fluid),
        EntryStack.codec().fieldOf("result").forGetter(BrewerDisplay::result),
        Identifier.CODEC.optionalFieldOf("id").forGetter(BrewerDisplay::id)
    ).apply(instance, BrewerDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BrewerDisplay> PACKET_CODEC = StreamCodec.composite(
        EntryIngredient.streamCodec(), BrewerDisplay::input,
        EntryIngredient.streamCodec(), BrewerDisplay::first,
        EntryIngredient.streamCodec(), BrewerDisplay::second,
        EntryIngredient.streamCodec(), BrewerDisplay::fluid,
        EntryStack.streamCodec(), BrewerDisplay::result,
        ByteBufCodecs.optional(Identifier.STREAM_CODEC), BrewerDisplay::id,
        BrewerDisplay::new
    );

    public static final DisplaySerializer<BrewerDisplay> SERIALIZER = DisplaySerializer.of(MAP_CODEC, PACKET_CODEC);

    public BrewerDisplay(ItemBrewingRecipe recipe, @Nullable Identifier id) {
        this(
            EntryIngredients.ofIngredient(recipe.input()),
            EntryIngredients.ofIngredient(recipe.firstIngredient()),
            recipe.secondIngredient().map(EntryIngredients::ofIngredient).orElse(EntryIngredient.empty()),
            EntryIngredient.empty(),
            EntryStacks.of(recipe.result().create()),
            Optional.ofNullable(id)
        );
    }

    public BrewerDisplay(FluidBrewingRecipe recipe, @Nullable Identifier id) {
        this(
            EntryIngredients.ofIngredient(recipe.input()),
            EntryIngredients.ofIngredient(recipe.firstIngredient()),
            recipe.secondIngredient().map(EntryIngredients::ofIngredient).orElse(EntryIngredient.empty()),
            entryIngredientOf(recipe.fluid()),
            EntryStacks.of(recipe.result().create()),
            Optional.ofNullable(id)
        );
    }

    private static EntryIngredient entryIngredientOf(FluidIngredient fluidIngredient) {
        var amount = FluidUnit.convert(fluidIngredient.getAmount(), fluidIngredient.getUnit(), FluidBridge.get().getFluidUnit());
        var stacks = fluidIngredient.fluid()
            .getFluids()
            .stream()
            .map(fluid -> EntryStacks.of(FluidStack.create(fluid, amount, fluidIngredient.components())))
            .toList();
        return EntryIngredient.of(stacks);
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(input, first, second, fluid);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(EntryIngredient.of(result));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return AdornReiServer.BREWER;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return id;
    }

    @Override
    public DisplaySerializer<? extends Display> getSerializer() {
        return SERIALIZER;
    }
}
