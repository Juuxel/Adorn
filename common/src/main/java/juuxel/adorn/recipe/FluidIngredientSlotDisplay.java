package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.platform.RecipeBridge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.crafting.display.DisplayContentsFactory;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.stream.Stream;

public record FluidIngredientSlotDisplay(FluidIngredient ingredient) implements SlotDisplay {
    public static final MapCodec<FluidIngredientSlotDisplay> MAP_CODEC = FluidIngredient.CODEC
        .fieldOf("ingredient")
        .xmap(FluidIngredientSlotDisplay::new, FluidIngredientSlotDisplay::ingredient);

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidIngredientSlotDisplay> PACKET_CODEC = FluidIngredient.PACKET_CODEC
        .map(FluidIngredientSlotDisplay::new, FluidIngredientSlotDisplay::ingredient);

    public static final Type<FluidIngredientSlotDisplay> SERIALIZER = new Type<>(MAP_CODEC, PACKET_CODEC);

    @Override
    public <T> Stream<T> resolve(ContextMap parameters, DisplayContentsFactory<T> factory) {
        return RecipeBridge.get().appendFluidIngredientStacks(ingredient, parameters, factory);
    }

    @Override
    public Type<? extends SlotDisplay> type() {
        return SERIALIZER;
    }
}
