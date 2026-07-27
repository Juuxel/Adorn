package juuxel.adorn.platform;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

@InlineServices
public interface BlockBridge {
    boolean hasNextOxidationLevelForTicking(BlockState state);

    float getSlipperiness(BlockState state, LevelReader world, BlockPos pos, @Nullable Entity entity);

    @InlineServices.Getter
    static BlockBridge get() {
        return Services.load(BlockBridge.class);
    }
}
