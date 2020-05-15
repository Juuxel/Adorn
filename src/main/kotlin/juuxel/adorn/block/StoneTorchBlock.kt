@file:Suppress("DEPRECATION")
package juuxel.adorn.block

import java.util.Random
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.TorchBlock
import net.minecraft.block.WallTorchBlock
import net.minecraft.block.Waterloggable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.Items
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class StoneTorchBlock : TorchBlock(createSettings(), ParticleTypes.FLAME), Waterloggable, BlockWithDescription {
    init {
        defaultState = defaultState.with(LIT, true)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(LIT, WATERLOGGED)
    }

    override fun tryFillWithFluid(world: WorldAccess, pos: BlockPos, state: BlockState, fluidState: FluidState) =
        super.tryFillWithFluid(world, pos, state, fluidState).also {
            if (it) {
                world.setBlockState(pos, world.getBlockState(pos).with(LIT, false), 3)
            }
        }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult?
    ): ActionResult = onUseImpl(state, world, pos, player, hand) {
        super.onUse(state, world, pos, player, hand, hitResult)
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (state[LIT]) {
            super.randomDisplayTick(state, world, pos, random)
        }
    }

    override fun getPlacementState(context: ItemPlacementContext) =
        super.getPlacementState(context)?.let {
            it.with(LIT, it.fluidState.isEmpty)
                .with(
                    Properties.WATERLOGGED,
                    context.world.getFluidState(context.blockPos).fluid == Fluids.WATER
                )
        }

    override fun getFluidState(state: BlockState) =
        if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
        else super.getFluidState(state)

    class Wall(settings: Settings) : WallTorchBlock(settings, ParticleTypes.FLAME), Waterloggable {
        init {
            defaultState = defaultState.with(LIT, true).with(WATERLOGGED, false)
        }

        override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
            super.appendProperties(builder)
            builder.add(LIT, WATERLOGGED)
        }

        override fun tryFillWithFluid(world: WorldAccess, pos: BlockPos, state: BlockState, fluidState: FluidState) =
            super.tryFillWithFluid(world, pos, state, fluidState).also {
                if (it) {
                    world.setBlockState(pos, world.getBlockState(pos).with(LIT, false), 3)
                }
            }

        override fun onUse(
            state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hitResult: BlockHitResult?
        ): ActionResult = onUseImpl(state, world, pos, player, hand) {
            super.onUse(state, world, pos, player, hand, hitResult)
        }

        override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
            if (state[LIT]) {
                super.randomDisplayTick(state, world, pos, random)
            }
        }

        override fun getPlacementState(context: ItemPlacementContext) =
            super.getPlacementState(context)?.let {
                it.with(LIT, it.fluidState.isEmpty)
                    .with(
                        Properties.WATERLOGGED,
                        context.world.getFluidState(context.blockPos).fluid == Fluids.WATER
                    )
            }

        override fun getFluidState(state: BlockState) =
            if (state[WATERLOGGED]) Fluids.WATER.getStill(false)
            else super.getFluidState(state)
    }

    companion object {
        val LIT = Properties.LIT
        val WATERLOGGED = Properties.WATERLOGGED

        internal fun createSettings(): Settings =
            FabricBlockSettings.copyOf(Blocks.TORCH)
                .sounds(BlockSoundGroup.STONE)
                .lightLevel { if (it[LIT]) 15 else 0 }

        private inline fun onUseImpl(
            state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand,
            superCallback: () -> ActionResult
        ): ActionResult {
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
                return ActionResult.SUCCESS
            }

            return superCallback()
        }
    }
}
