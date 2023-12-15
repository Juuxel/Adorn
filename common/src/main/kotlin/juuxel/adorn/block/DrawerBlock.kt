@file:Suppress("DEPRECATION")

package juuxel.adorn.block

import com.mojang.serialization.MapCodec
import juuxel.adorn.block.entity.DrawerBlockEntity
import juuxel.adorn.block.entity.SimpleContainerBlockEntity
import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.util.buildShapeRotationsFromNorth
import juuxel.adorn.util.mergeShapeMaps
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class DrawerBlock(variant: BlockVariant) : VisibleBlockWithEntity(variant.createSettings().nonOpaque()), BlockWithDescription {
    override val descriptionKey = "block.adorn.drawer.description"

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState =
        defaultState.with(FACING, context.horizontalPlayerFacing.opposite)

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is DrawerBlockEntity) {
            PlatformBridges.menus.open(player, blockEntity, pos)
            player.incrementStat(AdornStats.OPEN_DRAWER)
        }

        return ActionResult.CONSUME
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        ItemScatterer.onStateReplaced(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
        SHAPES[state[FACING]]!!

    override fun mirror(state: BlockState, mirror: BlockMirror) =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun rotate(state: BlockState, rotation: BlockRotation) =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (stack.hasCustomName()) {
            (world.getBlockEntity(pos) as? LootableContainerBlockEntity)?.customName = stack.name
        }
    }

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        Menu.calculateComparatorOutput(world.getBlockEntity(pos))

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.DRAWER.instantiate(pos, state)

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        val entity = world.getBlockEntity(pos)
        if (entity is SimpleContainerBlockEntity) {
            entity.onScheduledTick()
        }
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = throw UnsupportedOperationException()

    companion object {
        val FACING = Properties.HORIZONTAL_FACING

        private val SHAPES: Map<Direction, VoxelShape> = mergeShapeMaps(
            buildShapeRotationsFromNorth(0, 0, 3, 16, 16, 16),
            // Top drawer
            buildShapeRotationsFromNorth(1, 9, 1, 15, 15, 3),
            // Bottom drawer
            buildShapeRotationsFromNorth(1, 1, 1, 15, 7, 3)
        )
    }
}
