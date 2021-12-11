@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.StateManager
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.Random

class ChimneyBlock : AbstractChimneyBlock(Settings.copy(Blocks.BRICKS).ticksRandomly().nonOpaque()), BlockWithDescription {
    override val descriptionKey = "block.adorn.chimney.desc"

    init {
        defaultState = defaultState.with(SMOKE_TYPE, SmokeType.CAMPFIRE)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(SMOKE_TYPE)
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity?, hand: Hand?, hitResult: BlockHitResult?
    ): ActionResult {
        world.setBlockState(pos, state.cycle(SMOKE_TYPE))
        return ActionResult.SUCCESS
    }

    @Environment(EnvType.CLIENT)
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (state[CONNECTED]) return

        when (state[SMOKE_TYPE]!!) {
            SmokeType.CLASSIC -> for (i in 1..(3 + random.nextInt(2))) {
                world.addImportantParticle(
                    ParticleTypes.LARGE_SMOKE,
                    pos.x + 0.3 + random.nextDouble() * 0.4,
                    pos.y + 0.9,
                    pos.z + 0.3 + random.nextDouble() * 0.4,
                    0.0, 0.0, 0.0
                )
            }

            SmokeType.CAMPFIRE -> for (i in 1..(3 + random.nextInt(2))) {
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

    companion object {
        val SMOKE_TYPE = EnumProperty.of("smoke_type", SmokeType::class.java)
    }

    enum class SmokeType(private val id: String) : StringIdentifiable {
        CLASSIC("classic"), CAMPFIRE("campfire");

        override fun asString() = id
    }
}
