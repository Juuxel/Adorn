package juuxel.adorn.client;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@InlineServices
public interface FluidRenderingBridge {
    @Nullable TextureAtlasSprite getStillSprite(FluidReference volume);

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
