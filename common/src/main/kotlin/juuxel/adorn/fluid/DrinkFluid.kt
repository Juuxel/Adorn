package juuxel.adorn.fluid

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.Optional

class DrinkFluid(
    private val isStill: Boolean,
    private val flowing: () -> Fluid,
    private val still: () -> Fluid,
    private val block: () -> Block,
    private val bucket: () -> Item,
    val color: Int,
) : FlowableFluid() {
    init {
        if (!isStill) {
            defaultState = defaultState.with(LEVEL, 8)
        }
    }

    override fun getBucketItem(): Item = bucket()

    override fun canBeReplacedWith(state: FluidState, world: BlockView, pos: BlockPos, fluid: Fluid, direction: Direction): Boolean =
        false // TODO: would something else be better?

    override fun getTickRate(world: WorldView?): Int = 5
    override fun getBlastResistance(): Float = 100f
    override fun toBlockState(state: FluidState): BlockState =
        block().defaultState.with(FluidBlock.LEVEL, getBlockStateLevel(state))

    override fun isStill(state: FluidState): Boolean = isStill
    override fun getLevel(state: FluidState): Int = if (isStill) state[LEVEL] else 8
    override fun getFlowing(): Fluid = flowing()
    override fun getStill(): Fluid = still()
    override fun isInfinite(): Boolean = false

    override fun appendProperties(builder: StateManager.Builder<Fluid, FluidState>) {
        super.appendProperties(builder)

        if (!isStill) {
            builder.add(LEVEL)
        }
    }

    override fun beforeBreakingBlock(world: WorldAccess, pos: BlockPos, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null
        Block.dropStacks(state, world, pos, blockEntity)
    }

    override fun getFlowSpeed(world: WorldView): Int = 4
    override fun getLevelDecreasePerBlock(world: WorldView): Int = 1

    override fun matchesType(fluid: Fluid): Boolean =
        fluid === this || fluid === (if (isStill) flowing() else still())

    override fun getBucketFillSound(): Optional<SoundEvent> =
        Optional.of(SoundEvents.ITEM_BUCKET_FILL)
}
