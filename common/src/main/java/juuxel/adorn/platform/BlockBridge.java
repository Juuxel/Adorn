package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

@InlineServices
public interface BlockBridge {
    boolean hasNextOxidationLevelForTicking(BlockState state);

    float getSlipperiness(BlockState state, WorldView world, BlockPos pos, @Nullable Entity entity);

    @InlineServices.Getter
    static BlockBridge get() {
        return Services.load(BlockBridge.class);
    }
}
