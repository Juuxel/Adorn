package juuxel.adorn.mixin.fluidloggable;

import juuxel.adorn.block.*;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.towelette.api.Fluidloggable;

@Mixin({
        BubbleChimneyBlock.class,
        ChairBlock.class,
        ChimneyBlock.class,
        PlatformBlock.class,
        PostBlock.class,
        SofaBlock.class,
        StepBlock.class,
        StoneTorchBlock.class,
        StoneTorchBlock.Wall.class,
        TableBlock.class
})
public class FluidloggableMixin implements Fluidloggable {
}
