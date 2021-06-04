@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import juuxel.adorn.block.property.OptionalProperty
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.DyedCarpetBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.WorldAccess
import java.util.Random

abstract class CarpetedBlock(settings: Settings) : SeatBlock(settings) {
    init {
        if (isCarpetingEnabled()) {
            defaultState = defaultState.with(CARPET, CARPET.none)
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        if (isCarpetingEnabled()) builder.add(CARPET)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        if (isCarpetingEnabled())
            super.getPlacementState(context)!!.with(CARPET, getCarpetColor(context)?.let(CARPET::wrap) ?: CARPET.none)
        else
            super.getPlacementState(context)

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random?) {
        if (!isCarpetingEnabled()) return
        val carpet = state[CARPET]
        if (carpet.isPresent) {
            val carpetBlock = COLORS_TO_BLOCKS[carpet.value] ?: error("Unknown carpet state: $carpet")
            if (!carpetBlock.defaultState.canPlaceAt(world, pos)) {
                carpetBlock.onBreak(world, pos, state, null)
                dropStacks(carpetBlock.defaultState, world, pos)
                world.setBlockState(pos, state.with(CARPET, CARPET.none))
            }
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: WorldAccess, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if (isCarpetingEnabled()) {
            val carpet = state[CARPET]
            if (carpet.isPresent && !COLORS_TO_BLOCKS[carpet.value]!!.defaultState.canPlaceAt(world, pos))
                world.blockTickScheduler.schedule(pos, this, 1)
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getDroppedStacks(state: BlockState, builder: LootContext.Builder): List<ItemStack> =
        if (isCarpetingEnabled()) {
            super.getDroppedStacks(state, builder) +
                (COLORS_TO_BLOCKS[state[CARPET].value]?.defaultState?.getDroppedStacks(builder) ?: emptyList())
        } else {
            super.getDroppedStacks(state, builder)
        }

    open fun isCarpetingEnabled() = true

    open fun canStateBeCarpeted(state: BlockState) =
        isCarpetingEnabled() && state[CARPET] == CARPET.none

    companion object {
        val CARPET = OptionalProperty(EnumProperty.of("carpet", DyeColor::class.java))
        val CARPET_SHAPE = createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
        val COLORS_TO_BLOCKS: Map<DyeColor, Block> = mapOf(
            DyeColor.WHITE to Blocks.WHITE_CARPET,
            DyeColor.ORANGE to Blocks.ORANGE_CARPET,
            DyeColor.MAGENTA to Blocks.MAGENTA_CARPET,
            DyeColor.LIGHT_BLUE to Blocks.LIGHT_BLUE_CARPET,
            DyeColor.YELLOW to Blocks.YELLOW_CARPET,
            DyeColor.LIME to Blocks.LIME_CARPET,
            DyeColor.PINK to Blocks.PINK_CARPET,
            DyeColor.GRAY to Blocks.GRAY_CARPET,
            DyeColor.LIGHT_GRAY to Blocks.LIGHT_GRAY_CARPET,
            DyeColor.CYAN to Blocks.CYAN_CARPET,
            DyeColor.PURPLE to Blocks.PURPLE_CARPET,
            DyeColor.BLUE to Blocks.BLUE_CARPET,
            DyeColor.BROWN to Blocks.BROWN_CARPET,
            DyeColor.GREEN to Blocks.GREEN_CARPET,
            DyeColor.RED to Blocks.RED_CARPET,
            DyeColor.BLACK to Blocks.BLACK_CARPET
        )

        private fun getCarpetColor(context: ItemPlacementContext): DyeColor? =
            when (val block = context.world.getBlockState(context.blockPos).block) {
                is DyedCarpetBlock -> block.dyeColor
                else -> null
            }
    }
}
