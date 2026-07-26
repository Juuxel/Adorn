package juuxel.adorn.block.entity;

import juuxel.adorn.block.AdornBlockEntities;
import juuxel.adorn.fluid.FluidReference;
import juuxel.adorn.lib.AdornGameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.FluidTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.gameevent.GameEvent;
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
    public abstract boolean interactWithItem(ItemStack stack, Player player, InteractionHand hand);

    /**
     * Clears all fluids from this kitchen sink.
     * @return true if cleared and false otherwise
     */
    public abstract boolean clearFluidsWithSponge();

    /**
     * Called when this kitchen sink is filled with fluids.
     * Dispatches the game event and plays the sound.
     */
    protected void onFill(ItemStack stack, Player player) {
        level.gameEvent(player, GameEvent.FLUID_PLACE, worldPosition);
        player.playSound(getEmptySound(getFluidReference(), stack).event, 1f, 1f);
    }

    /**
     * Called when fluids are picked up from this kitchen sink.
     * Dispatches the game event and plays the sound.
     */
    protected void onPickUp(FluidReference fluid, ItemStack stack, Player player) {
        level.gameEvent(player, GameEvent.FLUID_PICKUP, worldPosition);
        player.level().playSound(player, player.getX(), player.getY(), player.getZ(), getFillSound(fluid, stack).event, SoundSource.BLOCKS, 1f, 1f);
    }

    protected FluidItemSound getFillSound(FluidReference fluid, ItemStack stack) {
        if (stack.is(Items.GLASS_BOTTLE)) {
            return new FluidItemSound(SoundEvents.BOTTLE_FILL, true);
        }

        return new FluidItemSound(fluid.getFluid().getPickupSound().orElse(SoundEvents.BUCKET_FILL), false);
    }

    protected FluidItemSound getEmptySound(FluidReference fluid, ItemStack stack) {
        if (isWaterBottle(stack)) {
            return new FluidItemSound(SoundEvents.BOTTLE_EMPTY, true);
        }

        return new FluidItemSound(fluid.getFluid().is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY, false);
    }

    protected static boolean isWaterBottle(ItemStack stack) {
        if (!stack.is(Items.POTION)) return false;
        var potionContents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        return potionContents.is(Potions.WATER);
    }

    protected void markDirtyAndSync() {
        setChanged();

        if (!level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    /**
     * Calculates the comparator output based on tank contents.
     */
    public abstract int calculateComparatorOutput();

    private static boolean isInfinite(Fluid fluid, ServerLevel world) {
        return fluid instanceof FlowingFluid flowable && flowable.canConvertToSource(world);
    }

    public static boolean supportsInfiniteExtraction(ServerLevel world, Fluid fluid) {
        return isInfinite(fluid, world) && world.getGameRules().get(AdornGameRules.INFINITE_KITCHEN_SINKS.get());
    }

    /**
     * A sound event containing coupled with whether it's preferred.
     * Used for a cursed priority system for fill/empty sounds:
     * bottle sounds are preferred by default, then Forge's fluid sounds and finally vanilla sounds.
     */
    public record FluidItemSound(
        SoundEvent event, boolean preferred) {
        public FluidItemSound orElse(@Nullable SoundEvent fallback) {
            if (preferred) return this;
            return fallback != null ? new FluidItemSound(fallback, true) : this;
        }
    }
}
