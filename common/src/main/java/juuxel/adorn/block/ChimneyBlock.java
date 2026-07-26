package juuxel.adorn.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public final class ChimneyBlock extends AbstractChimneyBlock implements BlockWithDescription {
    public static final EnumProperty<SmokeType> SMOKE_TYPE = EnumProperty.create("smoke_type", SmokeType.class);
    private static final String DESCRIPTION_KEY = "block.adorn.chimney.description";

    public ChimneyBlock(BlockBehaviour.Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(SMOKE_TYPE, SmokeType.CAMPFIRE));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SMOKE_TYPE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        world.setBlockAndUpdate(pos, state.cycle(SMOKE_TYPE));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(CONNECTED)) return;

        int count = 3 + random.nextInt(2);
        switch (state.getValue(SMOKE_TYPE)) {
            case CLASSIC -> {
                for (int i = 0; i < count; i++) {
                    world.addAlwaysVisibleParticle(
                        ParticleTypes.LARGE_SMOKE,
                        pos.getX() + 0.3 + random.nextDouble() * 0.4,
                        pos.getY() + 0.9,
                        pos.getZ() + 0.3 + random.nextDouble() * 0.4,
                        0.0, 0.0, 0.0
                    );
                }
            }

            case CAMPFIRE -> {
                for (int i = 0; i < count; i++) {
                    world.addAlwaysVisibleParticle(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE, true,
                        pos.getX() + 0.3 + random.nextDouble() * 0.4,
                        pos.getY() + 0.9 + random.nextDouble(),
                        pos.getZ() + 0.3 + random.nextDouble() * 0.4,
                        0.0, 0.07, 0.0
                    );
                }
            }
        }
    }

    public enum SmokeType implements StringRepresentable {
        CLASSIC("classic"), CAMPFIRE("campfire");

        private final String id;

        SmokeType(String id) {
            this.id = id;
        }

        @Override
        public String getSerializedName() {
            return id;
        }
    }
}
