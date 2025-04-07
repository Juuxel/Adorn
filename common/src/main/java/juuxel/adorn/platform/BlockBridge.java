package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.block.BlockState;

@InlineServices
public interface BlockBridge {
    boolean hasNextOxidationLevelForTicking(BlockState state);

    @InlineServices.Getter
    static BlockBridge get() {
        return Services.load(BlockBridge.class);
    }
}
