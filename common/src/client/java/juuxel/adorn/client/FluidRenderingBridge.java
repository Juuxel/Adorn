package juuxel.adorn.client;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

import java.util.List;

@InlineServices
public interface FluidRenderingBridge {
    default FluidModel getFluidModel(FluidReference volume) {
        var mc = Minecraft.getInstance();
        FluidState fluidState = volume.getFluid().defaultFluidState();
        return mc.getModelManager().getFluidStateModelSet().get(fluidState);
    }

    default @Nullable TextureAtlasSprite getStillSprite(FluidReference volume) {
        return getFluidModel(volume).stillMaterial().sprite();
    }

    int getColor(FluidReference volume, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos);

    default int getColor(FluidReference volume) {
        return getColor(volume, null, null);
    }

    boolean fillsFromTop(FluidReference volume);

    List<Component> getTooltip(FluidReference volume, TooltipFlag type, @Nullable Integer maxAmountInLitres);

    @InlineServices.Getter
    static FluidRenderingBridge get() {
        return Services.load(FluidRenderingBridge.class);
    }
}
