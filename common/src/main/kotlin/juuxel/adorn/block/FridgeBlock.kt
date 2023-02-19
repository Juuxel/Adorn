@file:Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")

package juuxel.adorn.block

import juuxel.adorn.block.entity.FridgeBlockEntity
import juuxel.adorn.util.buildShapeRotationsFromNorth
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LootableContainerBlockEntity
import net.minecraft.block.enums.DoorHinge
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.menu.Menu
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.EnumProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class FridgeBlock(settings: Settings) : VisibleBlockWithEntity(settings) {
    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(FACING, HINGE)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? =
        defaultState
            .with(FACING, ctx.playerFacing.opposite)
            .with(HINGE, getHinge(ctx))

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape =
        SHAPES[state[FACING]]!!

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hitResult: BlockHitResult?
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val blockEntity = world.getBlockEntity(pos)
        if (blockEntity is FridgeBlockEntity) {
            player.openMenu(blockEntity)
            // TODO: Stat
        }

        return ActionResult.CONSUME
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

    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState =
        if (mirror == BlockMirror.NONE) {
            state
        } else {
            state.rotate(mirror.getRotation(state[FACING])).cycle(HINGE)
        }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState =
        state.with(FACING, rotation.rotate(state[FACING]))

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        AdornBlockEntities.FRIDGE.instantiate(pos, state)

    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack) {
        if (stack.hasCustomName()) {
            (world.getBlockEntity(pos) as? LootableContainerBlockEntity)?.customName = stack.name
        }
    }

    override fun hasComparatorOutput(state: BlockState) = true

    override fun getComparatorOutput(state: BlockState, world: World, pos: BlockPos) =
        Menu.calculateComparatorOutput(world.getBlockEntity(pos))

    override fun <T : BlockEntity> getTicker(world: World, state: BlockState, type: BlockEntityType<T>): BlockEntityTicker<T>? =
        if (world.isClient) {
            checkType(type, AdornBlockEntities.FRIDGE, FridgeBlockEntity::clientTick)
        } else {
            null
        }

    companion object {
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING
        val HINGE: EnumProperty<DoorHinge> = Properties.DOOR_HINGE

        private val SHAPES = buildShapeRotationsFromNorth(0, 0, 3, 16, 16, 16)

        private fun getHinge(ctx: ItemPlacementContext): DoorHinge {
            val x = ctx.hitPos.x - ctx.blockPos.x
            val z = ctx.hitPos.z - ctx.blockPos.z

            return when (ctx.playerFacing) {
                Direction.NORTH -> if (x < 0.5) {
                    DoorHinge.LEFT
                } else {
                    DoorHinge.RIGHT
                }

                Direction.SOUTH -> if (x > 0.5) {
                    DoorHinge.LEFT
                } else {
                    DoorHinge.RIGHT
                }

                Direction.WEST -> if (z < 0.5) {
                    DoorHinge.LEFT
                } else {
                    DoorHinge.RIGHT
                }

                Direction.EAST -> if (z > 0.5) {
                    DoorHinge.LEFT
                } else {
                    DoorHinge.RIGHT
                }

                else -> throw IllegalArgumentException("Horizontal player facing is not ")
            }
        }
    }
}
