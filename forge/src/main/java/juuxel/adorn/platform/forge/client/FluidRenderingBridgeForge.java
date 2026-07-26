package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.FluidRenderingBridge;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.platform.forge.util.FluidTankReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.data.AtlasIds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class FluidRenderingBridgeForge implements FluidRenderingBridge {
    @Override
    public @Nullable TextureAtlasSprite getStillSprite(FluidReference volume) {
        var fluid = volume.getFluid();
        var atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);
        return atlas.getSprite(IClientFluidTypeExtensions.of(fluid).getStillTexture(FluidTankReference.toFluidStack(volume)));
    }

    @Override
    public int getColor(FluidReference volume, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        var fluid = volume.getFluid();
        if (world != null && pos != null) {
            return IClientFluidTypeExtensions.of(fluid).getTintColor(fluid.defaultFluidState(), world, pos);
        } else {
            return IClientFluidTypeExtensions.of(fluid).getTintColor(FluidTankReference.toFluidStack(volume));
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
