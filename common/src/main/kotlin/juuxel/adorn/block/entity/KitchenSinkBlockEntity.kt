package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.lib.AdornGameRules
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.tag.FluidTags
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

abstract class KitchenSinkBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(AdornBlockEntities.KITCHEN_SINK, pos, state) {
    /** A reference to the current fluid contents of this sink. */
    abstract val fluidReference: FluidReference

    /**
     * Tries to interact with this kitchen sink with a fluid container [stack].
     * @return true if inserted and false otherwise
     */
    abstract fun interactWithItem(stack: ItemStack, player: PlayerEntity, hand: Hand): Boolean

    /**
     * Clears all fluids from this kitchen sink.
     * @return true if cleared and false otherwise
     */
    abstract fun clearFluidsWithSponge(): Boolean

    /**
     * Called when this kitchen sink is filled with fluids.
     * Dispatches the game event and plays the sound.
     */
    protected fun onFill(stack: ItemStack, player: PlayerEntity) {
        val w = world!!
        if (!w.isClient) {
            w.emitGameEvent(player, GameEvent.FLUID_PLACE, pos)
            player.playSound(getEmptySound(fluidReference, stack).event, SoundCategory.BLOCKS, 1f, 1f)
        }
    }

    /**
     * Called when fluids are picked up from this kitchen sink.
     * Dispatches the game event and plays the sound.
     */
    protected fun onPickUp(fluid: FluidReference, stack: ItemStack, player: PlayerEntity) {
        val w = world!!
        if (!w.isClient) {
            w.emitGameEvent(player, GameEvent.FLUID_PICKUP, pos)
            player.playSound(getFillSound(fluid, stack).event, SoundCategory.BLOCKS, 1f, 1f)
        }
    }

    protected open fun getFillSound(fluid: FluidReference, stack: ItemStack): FluidItemSound {
        if (stack.isOf(Items.GLASS_BOTTLE)) {
            return FluidItemSound(SoundEvents.ITEM_BOTTLE_FILL, true)
        }

        return FluidItemSound(fluid.fluid.bucketFillSound.orElse(SoundEvents.ITEM_BUCKET_FILL), false)
    }

    protected open fun getEmptySound(fluid: FluidReference, stack: ItemStack): FluidItemSound {
        if (stack.isOf(Items.POTION) && PotionUtil.getPotion(stack) == Potions.WATER) {
            return FluidItemSound(SoundEvents.ITEM_BOTTLE_EMPTY, true)
        }

        return FluidItemSound(if (fluid.fluid.isIn(FluidTags.LAVA)) SoundEvents.ITEM_BUCKET_EMPTY_LAVA else SoundEvents.ITEM_BUCKET_EMPTY, false)
    }

    protected fun markDirtyAndSync() {
        markDirty()

        val w = world!!
        if (!w.isClient) {
            w.updateListeners(pos, cachedState, cachedState, 3)
        }
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> =
        BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

    /** Calculates the comparator output based on tank contents. */
    abstract fun calculateComparatorOutput(): Int

    companion object {
        private fun isInfinite(fluid: Fluid): Boolean =
            fluid is FlowableFluid && fluid.isInfinite

        fun supportsInfiniteExtraction(world: World, fluid: Fluid): Boolean =
            isInfinite(fluid) && world.gameRules.getBoolean(AdornGameRules.INFINITE_KITCHEN_SINKS)
    }

    /**
     * A sound event containing coupled with whether it's preferred.
     * Used for a cursed priority system for fill/empty sounds:
     * bottle sounds are preferred by default, then Forge's fluid sounds and finally vanilla sounds.
     */
    data class FluidItemSound(val event: SoundEvent, val preferred: Boolean) {
        fun orElse(fallback: SoundEvent?): FluidItemSound {
            if (preferred) return this
            return if (fallback != null) FluidItemSound(fallback, true) else this
        }
    }
}
