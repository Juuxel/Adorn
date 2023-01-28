package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.design.FurniturePart
import juuxel.adorn.util.ResettableLazy
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes

class DynamicFurnitureBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(AdornBlockEntities.DYNAMIC_FURNITURE, pos, state) {
    var parts: List<FurniturePart> = emptyList()
        private set
    private val shape: ResettableLazy<VoxelShape> = ResettableLazy(this::computeShape)

    private fun computeShape(): VoxelShape {
        if (parts.isEmpty()) return VoxelShapes.fullCube()

        return parts.fold(VoxelShapes.empty()) { shape, part ->
            part.withMinMax { minX, minY, minZ, maxX, maxY, maxZ ->
                VoxelShapes.combine(shape, VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ), BooleanBiFunction.OR)
            }
        }.simplify()
    }

    fun getShape(): VoxelShape = shape.get()

    override fun readNbt(nbt: NbtCompound) {
        parts = FurniturePart.fromNbt(nbt, NBT_PARTS) ?: emptyList()
        shape.reset()
    }

    override fun writeNbt(nbt: NbtCompound) {
        FurniturePart.writeNbt(nbt, NBT_PARTS, parts)
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> =
        BlockEntityUpdateS2CPacket.create(this)

    override fun toInitialChunkDataNbt(): NbtCompound = createNbt()

    companion object {
        private const val NBT_PARTS = "Parts"
    }
}
