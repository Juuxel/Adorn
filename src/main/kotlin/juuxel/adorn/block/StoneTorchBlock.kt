package juuxel.adorn.block

import io.github.juuxel.polyester.block.PolyesterBlock
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.Items
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateFactory
import net.minecraft.state.property.Properties
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World
import virtuoel.towelette.api.Fluidloggable
import java.util.Random

class StoneTorchBlock : TorchBlock(settings), PolyesterBlock, Fluidloggable {
    override val name = "stone_torch"
    override val itemSettings = null
    override val hasDescription = true

    init {
        defaultState = defaultState.with(LIT, true)
    }

    override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(LIT)
    }

    override fun tryFillWithFluid(world: IWorld, pos: BlockPos, state: BlockState, fluidState: FluidState) =
        super.tryFillWithFluid(world, pos, state, fluidState).also {
            if (it) {
                world.setBlockState(pos, world.getBlockState(pos).with(LIT, false), 3)
            }
        }

    override fun getLuminance(state: BlockState): Int {
        return if (state[LIT]) super.getLuminance(state) else 0
    }

    override fun activate(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult?
    ) = activateImpl(state, world, pos, player, hand) {
        super.activate(state, world, pos, player, hand, hitResult)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (state[LIT]) {
            super.randomDisplayTick(state, world, pos, random)
        }
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)?.let { it.with(LIT, it.fluidState.isEmpty) }

    class Wall(settings: Settings) : WallTorchBlock(settings), PolyesterBlock, Fluidloggable {
        override val name = "wall_stone_torch"
        override val itemSettings = null

        override fun appendProperties(builder: StateFactory.Builder<Block, BlockState>) {
            super.appendProperties(builder)
            builder.add(LIT)
        }

        override fun tryFillWithFluid(world: IWorld, pos: BlockPos, state: BlockState, fluidState: FluidState) =
            super.tryFillWithFluid(world, pos, state, fluidState).also {
                if (it) {
                    world.setBlockState(pos, world.getBlockState(pos).with(LIT, false), 3)
                }
            }

        override fun getLuminance(state: BlockState): Int {
            return if (state[LIT]) super.getLuminance(state) else 0
        }

        override fun activate(
            state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult?
        ) = activateImpl(state, world, pos, player, hand) {
            super.activate(state, world, pos, player, hand, hitResult)
        }

        override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
            if (state[LIT]) {
                super.randomDisplayTick(state, world, pos, random)
            }
        }

        override fun getPlacementState(context: ItemPlacementContext) =
            super.getPlacementState(context)?.let { it.with(LIT, it.fluidState.isEmpty) }
    }

    companion object {
        val LIT = Properties.LIT
        internal val settings = FabricBlockSettings.copy(Blocks.TORCH)
            .lightLevel(15)
            .sounds(BlockSoundGroup.STONE)
            .build()

        private inline fun activateImpl(
            state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand,
            superCallback: () -> Boolean
        ): Boolean {
            val stack = player.getStackInHand(hand)
            if (!state[LIT] && stack.item == Items.FLINT_AND_STEEL) {
                if (world.getFluidState(pos).isEmpty) {
                    world.setBlockState(pos, state.with(LIT, true))
                    world.playSound(
                        pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
                        SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS,
                        1f, 1f, false
                    )
                } else {
                    world.playSound(
                        pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
                        SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS,
                        1f, 1f, false
                    )
                }

                stack.damage(1, player) {}
                return true
            }

            return superCallback()
        }
    }
}
