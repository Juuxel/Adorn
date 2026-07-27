package juuxel.adorn.client;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.util.FluidTankReference;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class FluidRenderingBridgeNeo implements FluidRenderingBridge {
    @Override
    public int getColor(FluidReference volume, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        var fluidModel = getFluidModel(volume);
        var tintSource = fluidModel.fluidTintSource();

        if (tintSource == null) return 0xFF_FFFFFF;

        if (world != null && pos != null) {
            var fluidState = volume.getFluid().defaultFluidState();
            return tintSource.colorInWorld(fluidState, fluidState.createLegacyBlock(), world, pos);
        } else {
            return tintSource.colorAsStack(FluidTankReference.toFluidStack(volume));
        }
    }

    @Override
    public boolean fillsFromTop(FluidReference volume) {
        var fluid = volume.getFluid();
        return fluid.getFluidType().isLighterThanAir();
    }

    @Override
    public List<Component> getTooltip(FluidReference volume, TooltipFlag type, @Nullable Integer maxAmountInLitres) {
        List<Component> result = new ArrayList<>();
        var fluid = volume.getFluid();
        var stack = FluidTankReference.toFluidStack(volume);
        var name = stack.getHoverName();
        result.add(Component.empty().append(name).withStyle(fluid.getFluidType().getRarity(stack).getStyleModifier()));

        if (maxAmountInLitres != null) {
            result.add(volume.getAmountText(maxAmountInLitres, FluidUnit.LITRE));
        } else {
            result.add(volume.getAmountText());
        }

        // Append ID if advanced
        if (type.isAdvanced()) {
            result.add(Component.literal(BuiltInRegistries.FLUID.getKey(fluid).toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
        return result;
    }
}
