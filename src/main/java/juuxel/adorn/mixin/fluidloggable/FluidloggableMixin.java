package juuxel.adorn.mixin.fluidloggable;

import juuxel.adorn.block.*;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.towelette.api.Fluidloggable;

@Mixin({
        ChairBlock.class,
        ChimneyBlock.class,
        CoffeeTableBlock.class,
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
public class FluidloggableMixin implements Fluidloggable {
}
