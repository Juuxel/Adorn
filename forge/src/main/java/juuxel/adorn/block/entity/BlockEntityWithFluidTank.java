package juuxel.adorn.block.entity;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public interface BlockEntityWithFluidTank {
    ResourceHandler<FluidResource> getTank();
}
