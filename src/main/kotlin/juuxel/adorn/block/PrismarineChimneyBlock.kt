package juuxel.adorn.block

import juuxel.adorn.item.ItemWithDescription
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BubbleColumnBlock
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.tag.FluidTags
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import java.util.Random

open class PrismarineChimneyBlock(settings: Settings) : AbstractChimneyBlock(settings) {
    @OnlyIn(Dist.CLIENT)
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (!state.fluidState.isIn(FluidTags.WATER) || state[CONNECTED]) return

        val x = pos.x + 0.5
        val y = pos.y + 0.9
        val z = pos.z + 0.5

        for (i in 1..3) {
            world.addImportantParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0.0, 0.0, 0.0)
        }
    }

    @OnlyIn(Dist.CLIENT)
    override fun appendTooltip(
        stack: ItemStack, world: BlockView?, tooltip: MutableList<Text>, context: TooltipContext
    ) {
        tooltip += ItemWithDescription.createDescriptionText("block.adorn.prismarine_chimney.desc")
    }

    class WithColumn(val drag: Boolean, settings: Settings) : PrismarineChimneyBlock(settings) {
        override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
            BubbleColumnBlock.update(world, pos.up(), drag)
        }

        override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, moved: Boolean) {
            world.blockTickScheduler.schedule(pos, this, 20)
        }

        override fun neighborUpdate(
            state: BlockState, world: World, pos: BlockPos, neighbor: Block, neighborPos: BlockPos, moved: Boolean
        ) {
            world.blockTickScheduler.schedule(pos, this, 20)
        }

        @OnlyIn(Dist.CLIENT)
        override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
            if (!drag) {
                super.randomDisplayTick(state, world, pos, random)
            }
        }
    }
}
