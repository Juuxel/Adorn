package juuxel.adorn.platform.forge.block.entity

import juuxel.adorn.block.entity.KitchenSinkBlockEntity
import juuxel.adorn.fluid.FluidReference
import juuxel.adorn.platform.forge.util.FluidTankReference
import juuxel.adorn.platform.forge.util.toFluidStack
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraftforge.common.SoundActions
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidType
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import kotlin.math.min

class KitchenSinkBlockEntityForge(pos: BlockPos, state: BlockState) : KitchenSinkBlockEntity(pos, state) {
    val tank: FluidTank = object : FluidTank(FluidType.BUCKET_VOLUME) {
        override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction?): FluidStack {
            return if (supportsInfiniteExtraction(world!!, fluid.fluid)) {
                FluidStack(fluid, min(fluidAmount, maxDrain))
            } else {
                super.drain(maxDrain, action)
            }
        }

        override fun onContentsChanged() {
            markDirtyAndSync()
        }
    }

    override val fluidReference: FluidReference = FluidTankReference(tank)
    private val tankHolder = LazyOptional.of { tank }

    override fun interactWithItem(stack: ItemStack, player: PlayerEntity, hand: Hand): Boolean {
        if (tank.space > 0) {
            // The player in the tryEmpty/FillContainer calls is only used for sound.
            val result = FluidUtil.tryEmptyContainer(stack, tank, tank.space, null, true)

            if (result.isSuccess) {
                onFill(stack, player)
                setStackOrInsert(player, hand, result.result)
                markDirtyAndSync()
                return true
            }
        }

        // Store before filling the item from the tank
        val tankFluid = fluidReference.createSnapshot()
        val result = FluidUtil.tryFillContainer(stack, tank, tank.fluidAmount, null, true)

        if (result.isSuccess) {
            onPickUp(tankFluid, stack, player)
            setStackOrInsert(player, hand, result.result)
            markDirtyAndSync()
            return true
        }

        // Special case bottles since they don't have a fluid handler.
        if (stack.isOf(Items.GLASS_BOTTLE)) {
            val drainingResult = tank.drain(BOTTLE_WATER, IFluidHandler.FluidAction.SIMULATE)
            if (drainingResult.amount >= BOTTLE_LITRES) {
                // Execute the draining for real this time.
                tank.drain(BOTTLE_WATER, IFluidHandler.FluidAction.EXECUTE)
                onPickUp(tankFluid, stack, player)
                val bottle = ItemStack(Items.POTION)
                PotionUtil.setPotion(bottle, Potions.WATER)
                setStackOrInsert(player, hand, bottle)
                return true
            }
        } else if (stack.isOf(Items.POTION)) {
            val spaceForWater = tank.fluid.isEmpty || (tank.fluid.isFluidEqual(BOTTLE_WATER) && tank.space >= BOTTLE_LITRES)

            if (spaceForWater && PotionUtil.getPotion(stack) == Potions.WATER) {
                onFill(stack, player)
                tank.fill(BOTTLE_WATER.copy(), IFluidHandler.FluidAction.EXECUTE)
                setStackOrInsert(player, hand, ItemStack(Items.GLASS_BOTTLE))
                markDirtyAndSync()
                return true
            }
        }

        return false
    }

    private fun setStackOrInsert(player: PlayerEntity, hand: Hand, stack: ItemStack) {
        val current = player.getStackInHand(hand)
        current.decrement(1)

        if (current.isEmpty) {
            player.setStackInHand(hand, stack)
        } else {
            player.inventory.offerOrDrop(stack)
        }
    }

    override fun clearFluidsWithSponge(): Boolean {
        if (!tank.fluid.fluid.isIn(FluidTags.WATER) || tank.fluid.amount == 0) return false

        tank.fluid.amount = 0
        markDirtyAndSync()
        return true
    }

    override fun getFillSound(fluid: FluidReference, stack: ItemStack): FluidItemSound =
        super.getFillSound(fluid, stack).orElse(fluid.fluid.fluidType.getSound(fluid.toFluidStack(), SoundActions.BUCKET_FILL))

    override fun getEmptySound(fluid: FluidReference, stack: ItemStack): FluidItemSound =
        super.getEmptySound(fluid, stack).orElse(fluid.fluid.fluidType.getSound(fluid.toFluidStack(), SoundActions.BUCKET_EMPTY))

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        tank.readFromNBT(nbt)
    }

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        tank.writeToNBT(nbt)
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return tankHolder.cast()
        }

        return super.getCapability(cap, side)
    }

    override fun calculateComparatorOutput(): Int =
        if (tank.isEmpty) {
            0
        } else {
            1 + MathHelper.floor(14 * tank.fluidAmount.toFloat() / tank.capacity.toFloat())
        }

    companion object {
        // Bottles are 250 l in Adorn *on Forge*.
        private const val BOTTLE_LITRES = 250
        private val BOTTLE_WATER = FluidStack(Fluids.WATER, BOTTLE_LITRES)
    }
}
