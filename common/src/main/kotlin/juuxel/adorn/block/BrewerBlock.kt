package juuxel.adorn.block

import juuxel.adorn.block.entity.BrewerBlockEntity
import juuxel.adorn.lib.AdornStats
import juuxel.adorn.util.buildShapeRotationsFromNorth
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.Random

class BrewerBlock(settings: Settings) : VisibleBlockWithEntity(settings) {
    init {
        defaultState = defaultState.with(HAS_MUG, false).with(ACTIVE, false)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState =
        defaultState.with(FACING, ctx.playerFacing.opposite)

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is BrewerBlockEntity) {
            player.openMenu(blockEntity)
            player.incrementStat(AdornStats.OPEN_BREWER)
        }

        return ActionResult.CONSUME
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
        SHAPES[state[FACING]]!!

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (stack.hasCustomName()) {
            (world.getBlockEntity(pos) as? LootableContainerBlockEntity)?.customName = stack.name
        }
    }

    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        if (!state.isOf(newState.block)) {
            val entity = world.getBlockEntity(pos)

            if (entity is Inventory) {
                ItemScatterer.spawn(world, pos, entity)
                world.updateComparators(pos, this)
            }

            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.BREWER.instantiate(pos, state)

    override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? =
        if (!world.isClient) checkType(type, AdornBlockEntities.BREWER, BrewerBlockEntity::tick) else null

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        state.rotate(mirror.getRotation(state[FACING]))

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING, HAS_MUG, ACTIVE)
    }

    override fun hasComparatorOutput(state: BlockState): Boolean = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos): Int =
        (world.getBlockEntity(pos) as? BrewerBlockEntity)?.calculateComparatorOutput() ?: 0

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (state[ACTIVE] && random.nextInt(3) == 0) {
            val facing = state[FACING]
            val x = pos.x + 0.5 + random.nextDouble() * RANDOM_CLOUD_OFFSET + facing.offsetX * FACING_CLOUD_OFFSET
            val y = pos.y + 0.37 + random.nextDouble() * RANDOM_CLOUD_OFFSET
            val z = pos.z + 0.5 + random.nextDouble() * RANDOM_CLOUD_OFFSET + facing.offsetZ * FACING_CLOUD_OFFSET
            world.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0, 0.0, 0.0)
        }
    }

    companion object {
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
        val HAS_MUG: BooleanProperty = BooleanProperty.of("has_mug")
        val ACTIVE: BooleanProperty = BooleanProperty.of("active")
        private val SHAPES = buildShapeRotationsFromNorth(4, 0, 2, 12, 14, 12)
        private const val RANDOM_CLOUD_OFFSET = 0.0625
        private const val FACING_CLOUD_OFFSET = 0.2
    }
}
