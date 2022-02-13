package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
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
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.tag.FluidTags
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos

abstract class KitchenSinkBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(AdornBlockEntities.KITCHEN_SINK, pos, state) {
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

    protected fun getFillSound(fluid: Fluid, stack: ItemStack): SoundEvent {
        if (stack.isOf(Items.GLASS_BOTTLE)) {
            return SoundEvents.ITEM_BOTTLE_FILL
        }

        return fluid.bucketFillSound.orElse(SoundEvents.ITEM_BUCKET_FILL)
    }

    protected fun getEmptySound(fluid: Fluid, stack: ItemStack): SoundEvent {
        if (stack.isOf(Items.POTION) && PotionUtil.getPotion(stack) == Potions.WATER) {
            return SoundEvents.ITEM_BOTTLE_EMPTY
        }

        // Only used on Fabric, so it's fine that we hardcode.
        // See https://github.com/FabricMC/fabric/issues/1999
        return if (fluid.isIn(FluidTags.LAVA)) SoundEvents.ITEM_BUCKET_EMPTY_LAVA else SoundEvents.ITEM_BUCKET_EMPTY
    }

    protected fun markDirtyAndSync() {
        if (world!!.isClient) return

        markDirty()
        world!!.updateListeners(pos, cachedState, cachedState, 3)
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> =
        BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

    companion object {
        fun isInfinite(fluid: Fluid): Boolean =
            fluid is FlowableFluid && fluid.isInfinite
    }
}
