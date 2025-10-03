package juuxel.adorn.platform.forge.block.entity;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public interface BlockEntityWithFluidTank {
    ResourceHandler<FluidResource> getTank();
}
