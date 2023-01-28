package juuxel.adorn.block

import juuxel.adorn.block.entity.DynamicFurnitureBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class DynamicFurnitureBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        DynamicFurnitureBlockEntity(pos, state)

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext): VoxelShape {
        val blockEntity = world.getBlockEntity(pos)

        if (blockEntity is DynamicFurnitureBlockEntity) {
            return blockEntity.getShape()
        }

        return VoxelShapes.fullCube()
    }
}
