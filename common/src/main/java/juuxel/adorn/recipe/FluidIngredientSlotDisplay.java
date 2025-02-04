package juuxel.adorn.recipe;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.fluid.FluidIngredient;
import juuxel.adorn.platform.RecipeBridge;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.display.DisplayedItemFactory;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.util.context.ContextParameterMap;

import java.util.stream.Stream;

public record FluidIngredientSlotDisplay(FluidIngredient ingredient) implements SlotDisplay {
    public static final MapCodec<FluidIngredientSlotDisplay> MAP_CODEC = FluidIngredient.CODEC
        .fieldOf("ingredient")
        .xmap(FluidIngredientSlotDisplay::new, FluidIngredientSlotDisplay::ingredient);

    public static final PacketCodec<RegistryByteBuf, FluidIngredientSlotDisplay> PACKET_CODEC = FluidIngredient.PACKET_CODEC
        .xmap(FluidIngredientSlotDisplay::new, FluidIngredientSlotDisplay::ingredient);

    public static final Serializer<FluidIngredientSlotDisplay> SERIALIZER = new Serializer<>(MAP_CODEC, PACKET_CODEC);

    @Override
    public <T> Stream<T> appendStacks(ContextParameterMap parameters, DisplayedItemFactory<T> factory) {
        return RecipeBridge.get().appendFluidIngredientStacks(ingredient, parameters, factory);
    }

    @Override
    public Serializer<? extends SlotDisplay> serializer() {
        return SERIALIZER;
    }
}
