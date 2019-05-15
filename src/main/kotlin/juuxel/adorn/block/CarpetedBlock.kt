package juuxel.adorn.block

import juuxel.adorn.block.property.OptionalProperty
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.CarpetBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateFactory
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.loot.context.LootContext
import java.util.Random

abstract class CarpetedBlock(settings: Settings) : SeatBlock(settings) {
    init {
        defaultState = defaultState.with(CARPET, CARPET.none)
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(CARPET)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)!!.with(CARPET, getCarpetColor(context)?.let(CARPET::wrap) ?: CARPET.none)

    override fun onScheduledTick(state: BlockState, world: World, pos: BlockPos, random: Random?) {
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
        world: IWorld, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        val carpet = state[CARPET]
        if (carpet.isPresent && !COLORS_TO_BLOCKS[carpet.value]!!.defaultState.canPlaceAt(world, pos))
            world.blockTickScheduler.schedule(pos, this, 1)

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getDroppedStacks(state: BlockState, builder: LootContext.Builder): List<ItemStack> {
        return super.getDroppedStacks(state, builder) +
                (COLORS_TO_BLOCKS[state[CARPET].value]?.defaultState?.getDroppedStacks(builder) ?: emptyList())
    }

    companion object {
        val CARPET = OptionalProperty(EnumProperty.create("carpet", DyeColor::class.java))
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
                is CarpetBlock -> block.color
                else -> null
            }
    }
}
