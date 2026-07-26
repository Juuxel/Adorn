package juuxel.adorn.client;

import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.util.FluidStorageReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class FluidRenderingBridgeFabric implements FluidRenderingBridge {
    public static final FluidRenderingBridgeFabric INSTANCE = new FluidRenderingBridgeFabric();

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable TextureAtlasSprite getStillSprite(FluidReference volume) {
        return FluidVariantRendering.getSprite(FluidStorageReference.toFluidVariant(volume));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getColor(FluidReference volume, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        return FluidVariantRendering.getColor(FluidStorageReference.toFluidVariant(volume), world, pos);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean fillsFromTop(FluidReference volume) {
        return FluidVariantAttributes.isLighterThanAir(FluidStorageReference.toFluidVariant(volume));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public List<Component> getTooltip(FluidReference volume, TooltipFlag type, @Nullable Integer maxAmountInLitres) {
        var result = FluidVariantRendering.getTooltip(FluidStorageReference.toFluidVariant(volume), type);

        if (maxAmountInLitres != null) {
            result.add(1, volume.getAmountText(maxAmountInLitres, FluidUnit.LITRE));
        } else {
            result.add(1, volume.getAmountText());
        }

        return result;
    }
}
