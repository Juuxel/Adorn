package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.lib.AdornGameRules;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public abstract class KitchenSinkBlockEntity extends BlockEntity {
    public KitchenSinkBlockEntity(BlockPos pos, BlockState state) {
        super(AdornBlockEntities.KITCHEN_SINK.get(), pos, state);
    }

    /**
     * A reference to the current fluid contents of this sink.
     */
    public abstract FluidReference getFluidReference();

    /**
     * Tries to interact with this kitchen sink with a fluid container stack.
     * @return true if inserted and false otherwise
     */
    public abstract boolean interactWithItem(ItemStack stack, PlayerEntity player, Hand hand);

    /**
     * Clears all fluids from this kitchen sink.
     * @return true if cleared and false otherwise
     */
    public abstract boolean clearFluidsWithSponge();

    /**
     * Called when this kitchen sink is filled with fluids.
     * Dispatches the game event and plays the sound.
     */
    protected void onFill(ItemStack stack, PlayerEntity player) {
        world.emitGameEvent(player, GameEvent.FLUID_PLACE, pos);
        player.playSound(getEmptySound(getFluidReference(), stack).event, 1f, 1f);
    }

    /**
     * Called when fluids are picked up from this kitchen sink.
     * Dispatches the game event and plays the sound.
     */
    protected void onPickUp(FluidReference fluid, ItemStack stack, PlayerEntity player) {
        world.emitGameEvent(player, GameEvent.FLUID_PICKUP, pos);
        player.getEntityWorld().playSound(player, player.getX(), player.getY(), player.getZ(), getFillSound(fluid, stack).event, SoundCategory.BLOCKS, 1f, 1f);
    }

    protected FluidItemSound getFillSound(FluidReference fluid, ItemStack stack) {
        if (stack.isOf(Items.GLASS_BOTTLE)) {
            return new FluidItemSound(SoundEvents.ITEM_BOTTLE_FILL, true);
        }

        return new FluidItemSound(fluid.getFluid().getBucketFillSound().orElse(SoundEvents.ITEM_BUCKET_FILL), false);
    }

    protected FluidItemSound getEmptySound(FluidReference fluid, ItemStack stack) {
        if (isWaterBottle(stack)) {
            return new FluidItemSound(SoundEvents.ITEM_BOTTLE_EMPTY, true);
        }

        return new FluidItemSound(fluid.getFluid().isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY, false);
    }

    protected static boolean isWaterBottle(ItemStack stack) {
        if (!stack.isOf(Items.POTION)) return false;
        var potionContents = stack.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
        return potionContents.matches(Potions.WATER);
    }

    protected void markDirtyAndSync() {
        markDirty();

        if (!world.isClient()) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createComponentlessNbt(registries);
    }

    /**
     * Calculates the comparator output based on tank contents.
     */
    public abstract int calculateComparatorOutput();

    private static boolean isInfinite(Fluid fluid, ServerWorld world) {
        return fluid instanceof FlowableFluid flowable && flowable.isInfinite(world);
    }

    public static boolean supportsInfiniteExtraction(ServerWorld world, Fluid fluid) {
        return isInfinite(fluid, world) && world.getGameRules().getValue(AdornGameRules.INFINITE_KITCHEN_SINKS.get());
    }

    /**
     * A sound event containing coupled with whether it's preferred.
     * Used for a cursed priority system for fill/empty sounds:
     * bottle sounds are preferred by default, then Forge's fluid sounds and finally vanilla sounds.
     */
    public record FluidItemSound(SoundEvent event, boolean preferred) {
        public FluidItemSound orElse(@Nullable SoundEvent fallback) {
            if (preferred) return this;
            return fallback != null ? new FluidItemSound(fallback, true) : this;
        }
    }
}
