package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.BrewerBlock
import juuxel.adorn.item.AdornItems
import juuxel.adorn.menu.BrewerMenu
import juuxel.adorn.recipe.AdornRecipes
import juuxel.adorn.recipe.BrewingRecipe
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.ItemScatterer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World

open class BrewerBlockEntity(pos: BlockPos, state: BlockState) : BaseContainerBlockEntity(AdornBlockEntities.BREWER, pos, state, 3), SidedInventory {
    private var progress: Int = 0
    private val propertyDelegate = object : PropertyDelegate {
        override fun get(index: Int): Int = when (index) {
            0 -> progress
            else -> throw IllegalArgumentException("Unknown property: $index")
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> progress = value
                else -> throw IllegalArgumentException("Unknown property: $index")
            }
        }

        override fun size(): Int = 1
    }

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler =
        BrewerMenu(syncId, playerInventory, this, propertyDelegate)

    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        nbt.putInt(NBT_PROGRESS, progress)
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        progress = nbt.getInt(NBT_PROGRESS)
    }

    override fun getAvailableSlots(side: Direction): IntArray {
        val facing = cachedState[BrewerBlock.FACING]

        return when (side) {
            facing.rotateYClockwise() -> intArrayOf(LEFT_INGREDIENT_SLOT)
            facing.rotateYCounterclockwise() -> intArrayOf(RIGHT_INGREDIENT_SLOT)
            Direction.UP, Direction.DOWN -> intArrayOf(INPUT_SLOT)
            else -> intArrayOf()
        }
    }

    // TODO: Capabilities?????
    override fun canInsert(slot: Int, stack: ItemStack, side: Direction?) =
        side != Direction.DOWN && (side != Direction.UP || stack.isOf(AdornItems.MUG))

    override fun canExtract(slot: Int, stack: ItemStack, side: Direction) =
        side == Direction.DOWN

    fun calculateComparatorOutput(): Int {
        val progressFraction = progress.toFloat() / MAX_PROGRESS
        val level = progressFraction * 15
        return MathHelper.ceil(level)
    }

    companion object {
        private const val NBT_PROGRESS = "Progress"
        const val INPUT_SLOT = 0
        const val LEFT_INGREDIENT_SLOT = 1
        const val RIGHT_INGREDIENT_SLOT = 2
        const val MAX_PROGRESS = 200

        fun tick(world: World, pos: BlockPos, state: BlockState, brewer: BrewerBlockEntity) {
            var dirty = false
            val hasMug = !brewer.getStack(INPUT_SLOT).isEmpty

            if (hasMug != state[BrewerBlock.HAS_MUG]) {
                world.setBlockState(pos, state.with(BrewerBlock.HAS_MUG, hasMug))
            }

            val recipe: BrewingRecipe? = world.recipeManager.getFirstMatch(AdornRecipes.BREWING_TYPE, brewer, world).orElse(null)

            fun decrementIngredient(slot: Int) {
                val stack = brewer.getStack(slot)
                val remainder = stack.item.recipeRemainder
                stack.decrement(1)

                if (remainder != null) {
                    if (stack.isEmpty) {
                        brewer.setStack(slot, ItemStack(remainder))
                    } else {
                        ItemScatterer.spawn(world, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, ItemStack(remainder))
                    }
                }
            }

            if (recipe != null && brewer.getStack(INPUT_SLOT).isOf(AdornItems.MUG)) {
                if (brewer.progress++ >= MAX_PROGRESS) {
                    decrementIngredient(LEFT_INGREDIENT_SLOT)
                    decrementIngredient(RIGHT_INGREDIENT_SLOT)
                    brewer.setStack(INPUT_SLOT, recipe.output.copy())
                }

                dirty = true
            } else {
                if (brewer.progress != 0) {
                    brewer.progress = 0
                    dirty = true
                }
            }

            if (dirty) {
                markDirty(world, pos, state)
            }
        }
    }
}
