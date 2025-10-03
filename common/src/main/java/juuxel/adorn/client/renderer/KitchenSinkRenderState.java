package juuxel.adorn.client.renderer;

import juuxel.adorn.fluid.FluidVolume;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;

public final class KitchenSinkRenderState extends BlockEntityRenderState {
    public FluidVolume fluid;
    public int fluidColor;
    public float animationTime;
    public float tickProgress;
}
