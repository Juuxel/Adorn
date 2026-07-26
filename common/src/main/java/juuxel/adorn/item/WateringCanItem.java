package juuxel.adorn.item;

import com.mojang.serialization.Codec;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.fluid.FluidUnit;
import juuxel.adorn.fluid.StepMaximum;
import juuxel.adorn.lib.AdornSounds;
import juuxel.adorn.platform.FluidBridge;
import juuxel.adorn.util.Colors;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;

import java.util.function.Consumer;

public final class WateringCanItem extends Item {
    private static final int ITEM_BAR_STEPS = 13;
    private static final int MAX_WATER_LEVEL = 50;
    public static final int MAX_FERTILIZER_LEVEL = 32;
    private static final float WATER_LEVEL_DIVISOR = 1f / MAX_WATER_LEVEL;
    private static final int WATER_LEVELS_PER_BUCKET = 10;

    private static final StepMaximum FLUID_DRAIN_PREDICATE = new StepMaximum(0L, 1000L, 1000L / WATER_LEVELS_PER_BUCKET, FluidUnit.LITRE);

    public WateringCanItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        var stack = user.getItemInHand(hand);
        var success = false;

        var hitResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        int waterLevel = stack.getOrDefault(AdornComponentTypes.WATER_LEVEL.get(), 0);
        var pos = hitResult.getBlockPos();
        var state = world.getBlockState(pos);
        var block = state.getBlock();

        if (waterLevel < MAX_WATER_LEVEL) {
            // Check for drainable water

            // Note: we have a water check because we can't revert changes for non-water fluid sources
            if (block instanceof BucketPickup drainable && world.getFluidState(pos).is(Fluids.WATER)) {
                var drained = drainable.pickupBlock(user, world, pos, state);
                drainable.getPickupSound().ifPresent(sound -> user.playSound(sound, 1f, 1f));

                if (drained.is(Items.WATER_BUCKET)) {
                    waterLevel = Math.min(waterLevel + WATER_LEVELS_PER_BUCKET, MAX_WATER_LEVEL);
                    stack.set(AdornComponentTypes.WATER_LEVEL.get(), waterLevel);
                    success = true;
                }
            } else {
                var drained = FluidBridge.get().drain(world, pos, null, hitResult.getDirection().getOpposite(), Fluids.WATER, FLUID_DRAIN_PREDICATE);

                if (drained != null) {
                    long amount = FluidUnit.convert(drained.getAmount(), drained.getUnit(), FluidUnit.LITRE);
                    int levels = (int) (amount / FLUID_DRAIN_PREDICATE.getStep());
                    waterLevel = Math.min(waterLevel + levels, MAX_WATER_LEVEL);
                    stack.set(AdornComponentTypes.WATER_LEVEL.get(), waterLevel);
                    success = true;
                    user.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
                }
            }
        }

        if (!success && waterLevel > 0) {
            success = true;

            waterLevel--;
            stack.set(AdornComponentTypes.WATER_LEVEL.get(), waterLevel);
            world.gameEvent(user, GameEvent.ITEM_INTERACT_FINISH, pos);
            world.playSound(user, pos, AdornSounds.ITEM_WATERING_CAN_WATER.get(), SoundSource.PLAYERS);

            user.getCooldowns().addCooldown(stack, 10);

            var mut = new BlockPos.MutableBlockPos();
            for (int xo = -1; xo <= 1; xo++) {
                for (int zo = -1; zo <= 1; zo++) {
                    mut.set(pos.getX() + xo, pos.getY(), pos.getZ() + zo);
                    water(world, mut, user, stack);

                    if (world instanceof ServerLevel serverWorld) {
                        spawnParticlesAt(serverWorld, mut, hitResult.getLocation().y);
                    }
                }
            }
        }

        return success ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    private void water(Level world, BlockPos pos, Player player, ItemStack stack) {
        int fertilizerLevel = FertilizerLevel.get(stack);
        var state = world.getBlockState(pos);
        var block = state.getBlock();

        if (fertilizerLevel > 0 && world.getRandom().nextInt(9) == 0) {
            if (block instanceof BonemealableBlock fertilizable && fertilizable.isValidBonemealTarget(world, pos, state)) {
                if (world instanceof ServerLevel serverWorld && fertilizable.isBonemealSuccess(world, world.getRandom(), pos, state)) {
                    fertilizable.performBonemeal(serverWorld, world.getRandom(), pos, state);
                }

                world.levelEvent(player, LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 5);
            }

            stack.set(AdornComponentTypes.FERTILIZER_LEVEL.get(), FertilizerLevel.of(fertilizerLevel - 1));
        }

        if (!world.isClientSide()) {
            if (block instanceof FarmlandBlock) {
                waterFarmlandBlock(world, pos, state);
            } else if (!state.isCollisionShapeFullBlock(world, pos)) { // We can't water through full cubes
                var downPos = pos.below();
                var downState = world.getBlockState(downPos);

                if (downState.getBlock() instanceof FarmlandBlock) {
                    waterFarmlandBlock(world, downPos, downState);
                }
            }
        }
    }

    private void waterFarmlandBlock(Level world, BlockPos pos, BlockState state) {
        int moisture = state.getValue(FarmlandBlock.MOISTURE);

        if (moisture < FarmlandBlock.MAX_MOISTURE) {
            var moistureChange = world.getRandom().nextIntBetweenInclusive(2, 6);
            var newMoisture = Math.min(moisture + moistureChange, FarmlandBlock.MAX_MOISTURE);
            world.setBlock(pos, state.setValue(FarmlandBlock.MOISTURE, newMoisture), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        var waterLevel = Mth.clamp(stack.getOrDefault(AdornComponentTypes.WATER_LEVEL.get(), 0), 0, MAX_WATER_LEVEL);
        return Mth.lerpInt(WATER_LEVEL_DIVISOR * waterLevel, 0, ITEM_BAR_STEPS);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        var rg = Mth.clampedMap(
            FertilizerLevel.get(stack),
            // From:
            0f, MAX_FERTILIZER_LEVEL,
            // To:
            0.4f, 1f
        );
        return Colors.color(rg, rg, 1f);
    }

    private static void spawnParticlesAt(ServerLevel world, BlockPos pos, double y) {
        double px = pos.getX() + 0.3 + world.getRandom().nextDouble() * 0.4;
        double py = y + 0.1;
        double pz = pos.getZ() + 0.3 + world.getRandom().nextDouble() * 0.4;
        double vx = world.getRandom().nextDouble() * 0.2 - 0.1;
        double vy = 0.1;
        double vz = world.getRandom().nextDouble() * 0.2 - 0.1;
        world.sendParticles(ParticleTypes.SPLASH, px, py, pz, 4, vx, vy, vz, 0.5);
    }

    public record FertilizerLevel(int level) implements TooltipProvider {
        public static final Codec<FertilizerLevel> CODEC = Codec.INT.xmap(FertilizerLevel::of, FertilizerLevel::level);
        public static final FertilizerLevel ZERO = new FertilizerLevel(0);

        public static FertilizerLevel of(int level) {
            return level != 0 ? new FertilizerLevel(level) : ZERO;
        }

        public static int get(ItemStack stack) {
            return stack.getOrDefault(AdornComponentTypes.FERTILIZER_LEVEL.get(), ZERO).level;
        }

        @Override
        public void addToTooltip(TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
            var currentLevel = Component.literal(Integer.toString(level)).withStyle(ChatFormatting.DARK_AQUA);
            var maxLevel = Component.literal(Integer.toString(MAX_FERTILIZER_LEVEL)).withStyle(ChatFormatting.DARK_AQUA);
            textConsumer.accept(Component.translatable("item.adorn.watering_can.fertilizer", currentLevel, maxLevel).withStyle(ChatFormatting.GRAY));
        }
    }
}
