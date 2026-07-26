package juuxel.adorn.block;

import juuxel.adorn.platform.BlockBridge;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class OxidizableCopperPipeBlock extends CopperPipeBlock implements WeatheringCopper {
    private final WeatherState oxidationLevel;

    public OxidizableCopperPipeBlock(WeatheringCopper.WeatherState oxidationLevel, BlockBehaviour.Properties settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        changeOverTime(state, world, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return BlockBridge.get().hasNextOxidationLevelForTicking(state);
    }

    @Override
    public WeatherState getAge() {
        return oxidationLevel;
    }
}
