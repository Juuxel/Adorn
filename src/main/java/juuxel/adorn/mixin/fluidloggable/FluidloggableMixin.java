package juuxel.adorn.mixin.fluidloggable;

import juuxel.adorn.block.*;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({
        ChairBlock.class,
        ChimneyBlock.class,
        CoffeeTableBlock.class,
        PicketFenceBlock.class,
        PlatformBlock.class,
        PostBlock.class,
        PrismarineChimneyBlock.class,
        ShelfBlock.class,
        SofaBlock.class,
        StepBlock.class,
        StoneTorchBlock.class,
        StoneTorchBlock.Wall.class,
        TableBlock.class,
        TableLampBlock.class,
        TradingStationBlock.class
})
// TODO: Add this back with Towelette 4's API
public class FluidloggableMixin {
}
