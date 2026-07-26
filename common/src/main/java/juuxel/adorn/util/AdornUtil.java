package juuxel.adorn.util;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import org.slf4j.Logger;

public final class AdornUtil {
    private static final Logger LOGGER = Logging.logger();

    public static MutableComponent toTextWithCount(ItemStack stack) {
        return Component.translatable("text.adorn.item_stack_with_count", stack.getCount(), stack.getDisplayName());
    }

    public static BlockBehaviour.Properties copySettingsSafely(Block block) {
        var settings = BlockBehaviour.Properties.of();
        caughtProperty(block, "mapColor", () -> settings.mapColor(block.defaultMapColor()));
        caughtProperty(block, "luminance", () -> {
            int luminance = block.defaultBlockState().getLightEmission();
            settings.lightLevel(state -> luminance);
        });
        caughtProperty(block, "hardness", () -> settings.destroyTime(block.defaultBlockState().getDestroySpeed(null, null)));
        caughtProperty(block, "resistance", () -> settings.explosionResistance(block.getExplosionResistance()));
        caughtProperty(block, "velocityMultiplier", () -> settings.speedFactor(block.getSpeedFactor()));
        caughtProperty(block, "jumpVelocityMultiplier", () -> settings.jumpFactor(block.getJumpFactor()));
        caughtProperty(block, "slipperiness", () -> settings.friction(block.getFriction()));
        caughtProperty(block, "soundGroup", () -> settings.sound(block.defaultBlockState().getSoundType()));
        caughtProperty(block, "burnable", () -> {
            if (block.defaultBlockState().ignitedByLava()) {
                settings.ignitedByLava();
            }
        });
        return settings;
    }

    private static void caughtProperty(Block block, String name, Runnable fn) {
        try {
            fn.run();
        } catch (Exception e) {
            LOGGER.warn("[Adorn] Could not get block property {} from {}", name, BuiltInRegistries.BLOCK.getKey(block), e);
        }
    }

    public static Direction.Axis turnHorizontally(Direction.Axis axis) {
        return switch (axis) {
            case X -> Direction.Axis.Z;
            case Z -> Direction.Axis.X;
            case Y -> Direction.Axis.Y;
        };
    }

    public static ContainerLevelAccess menuContextOf(BlockEntity blockEntity) {
        return ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
    }

    public static <T> Iterable<Holder.Reference<T>> iterateEntries(HolderLookup<T> registry) {
        return () -> registry.listElements().iterator();
    }
}
