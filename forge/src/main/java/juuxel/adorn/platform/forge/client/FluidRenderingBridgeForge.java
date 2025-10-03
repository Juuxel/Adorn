package juuxel.adorn.platform.forge.client;

import juuxel.adorn.client.FluidRenderingBridge;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.platform.forge.util.FluidTankReference;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Atlases;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class FluidRenderingBridgeForge implements FluidRenderingBridge {
    @Override
    public @Nullable Sprite getStillSprite(FluidReference volume) {
        var fluid = volume.getFluid();
        var atlas = MinecraftClient.getInstance().getAtlasManager().getAtlasTexture(Atlases.BLOCKS);
        return atlas.getSprite(IClientFluidTypeExtensions.of(fluid).getStillTexture(FluidTankReference.toFluidStack(volume)));
    }

    @Override
    public int getColor(FluidReference volume, @Nullable BlockRenderView world, @Nullable BlockPos pos) {
        var fluid = volume.getFluid();
        if (world != null && pos != null) {
            return IClientFluidTypeExtensions.of(fluid).getTintColor(fluid.getDefaultState(), world, pos);
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
    public List<Text> getTooltip(FluidReference volume, TooltipType type, @Nullable Integer maxAmountInLitres) {
        List<Text> result = new ArrayList<>();
        var fluid = volume.getFluid();
        var stack = FluidTankReference.toFluidStack(volume);
        var name = stack.getHoverName();
        result.add(Text.empty().append(name).styled(fluid.getFluidType().getRarity(stack).getStyleModifier()));

        if (maxAmountInLitres != null) {
            result.add(volume.getAmountText(maxAmountInLitres, FluidUnit.LITRE));
        } else {
            result.add(volume.getAmountText());
        }

        // Append ID if advanced
        if (type.isAdvanced()) {
            result.add(Text.literal(Registries.FLUID.getId(fluid).toString()).formatted(Formatting.DARK_GRAY));
        }
        return result;
    }
}
