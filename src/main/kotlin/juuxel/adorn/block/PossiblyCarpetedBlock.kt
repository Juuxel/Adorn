package juuxel.adorn.block

import juuxel.adorn.block.entity.CarpetedBlockEntity
import juuxel.adorn.item.CarpetedTopPlacementContext
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateFactory
import net.minecraft.state.property.BooleanProperty
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World
import net.minecraft.world.loot.context.LootContext
import net.minecraft.world.loot.context.LootContextParameters
import java.util.Random

abstract class PossiblyCarpetedBlock(private val carpeted: Boolean, settings: Settings) : Block(settings), BlockEntityProvider {
    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
    }

    fun appendCarpetedProperty(builder: StateFactory.Builder<Block, BlockState>) {
        builder.add(HAS_CARPET)
    }

    private fun getCarpetedBE(world: ViewableWorld, pos: BlockPos): CarpetedBlockEntity? =
        if (carpeted) world.getBlockEntity(pos) as? CarpetedBlockEntity
        else null

    override fun getPlacementState(context: ItemPlacementContext) =
        if (carpeted) super.getPlacementState(context)!!.with(HAS_CARPET, context is CarpetedTopPlacementContext)
        else super.getPlacementState(context)!!

    override fun onScheduledTick(state: BlockState, world: World, pos: BlockPos, random: Random?) {
        val be = getCarpetedBE(world, pos)
        if (be != null) {
            if (!be.carpetState.canPlaceAt(world, pos)) {
                be.carpetState.block.onBreak(world, pos, state, null)
                dropStacks(be.carpetState, world, pos)
                world.setBlockState(pos, state.with(HAS_CARPET, false))
            }
        }
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction, neighborState: BlockState,
        world: IWorld, pos: BlockPos, neighborPos: BlockPos
    ): BlockState {
        if ((getCarpetedBE(world, pos)?.carpetState?.canPlaceAt(world, pos) == false))
            world.blockTickScheduler.schedule(pos, this, 1)

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
    }

    override fun getDroppedStacks(state: BlockState, builder: LootContext.Builder): List<ItemStack> {
        return super.getDroppedStacks(state, builder) +
                (getCarpetedBE(builder.world, builder.get(LootContextParameters.POSITION))
                    ?.getDroppedStacks(builder) ?: emptyList())
    }

    override fun createBlockEntity(view: BlockView) =
        if (carpeted) CarpetedBlockEntity()
        else null

    companion object {
        val HAS_CARPET = BooleanProperty.create("has_carpet")
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
        ).withDefault { Blocks.WHITE_CARPET }
    }
}
