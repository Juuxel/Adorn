package juuxel.adorn.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public final class ChimneyBlock extends AbstractChimneyBlock implements BlockWithDescription {
    public static final EnumProperty<SmokeType> SMOKE_TYPE = EnumProperty.of("smoke_type", SmokeType.class);
    private static final String DESCRIPTION_KEY = "block.adorn.chimney.description";

    public ChimneyBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(SMOKE_TYPE, SmokeType.CAMPFIRE));
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SMOKE_TYPE);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        world.setBlockState(pos, state.cycle(SMOKE_TYPE));
        return ActionResult.SUCCESS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(CONNECTED)) return;

        int count = 3 + random.nextInt(2);
        switch (state.get(SMOKE_TYPE)) {
            case CLASSIC -> {
                for (int i = 0; i < count; i++) {
                    world.addImportantParticleClient(
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
                    world.addImportantParticleClient(
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

    public enum SmokeType implements StringIdentifiable {
        CLASSIC("classic"), CAMPFIRE("campfire");

        private final String id;

        SmokeType(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }
    }
}
