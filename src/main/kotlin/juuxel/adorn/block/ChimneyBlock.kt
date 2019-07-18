package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.Waterloggable
import net.minecraft.entity.EntityContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.StateFactory
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.ViewableWorld
import net.minecraft.world.World
import java.util.*

class ChimneyBlock : Block(FabricBlockSettings.copy(Blocks.BRICKS).ticksRandomly().build()), PolyesterBlock, Waterloggable {
    override val name = "chimney"
    override val itemSettings = Item.Settings().group(ItemGroup.DECORATIONS)
    override val hasDescription = true

    init {
        defaultState = defaultState.with(SMOKE_TYPE, SmokeType.CAMPFIRE)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(SMOKE_TYPE, WATERLOGGED)
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)?.run {
            with(WATERLOGGED, context.world.getFluidState(context.blockPos).fluid == Fluids.WATER)
        }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?
    ): Boolean {
        world.setBlockState(pos, state.with(SMOKE_TYPE, state[SMOKE_TYPE].cycle()))
        return true
    }

    @Environment(EnvType.CLIENT)
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        when (state[SMOKE_TYPE]!!) {
            SmokeType.CLASSIC -> for (i in 1..3) {
                world.addImportantParticle(
                    ParticleTypes.LARGE_SMOKE,
                    pos.x + 0.5, pos.y + 0.9, pos.z + 0.5,
                    0.0, 0.0, 0.0
                )
            }

            SmokeType.CAMPFIRE -> for (i in 0 until 2 + random.nextInt(2)) {
                world.addImportantParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE, true,
                    pos.x + 0.3 + random.nextDouble() * 0.4,
                    pos.y + 0.9 + random.nextDouble(),
                    pos.z + 0.3 + random.nextDouble() * 0.4,
                    0.0, 0.07, 0.0
                )
            }
        }
    }

    override fun getTickRate(world: ViewableWorld?) = 15

    override fun getOutlineShape(state: BlockState?, view: BlockView?, pos: BlockPos?, context: EntityContext?) =
        SHAPE

    override fun isOpaque(state: BlockState?) = false

    companion object {
        val SMOKE_TYPE = EnumProperty.of("smoke_type", SmokeType::class.java)
        val WATERLOGGED = Properties.WATERLOGGED
        private val SHAPE = createCuboidShape(4.0, 0.0, 4.0, 12.0, 12.0, 12.0)
    }

    enum class SmokeType(private val id: String) : StringIdentifiable {
        CLASSIC("classic"), CAMPFIRE("campfire");

        override fun asString() = id

        fun cycle() = values()[(ordinal + 1) % values().size]
    }
}
