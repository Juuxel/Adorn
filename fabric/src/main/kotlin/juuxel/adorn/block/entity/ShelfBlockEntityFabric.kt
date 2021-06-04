package juuxel.adorn.block.entity

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.inventory.Inventories
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

class ShelfBlockEntityFabric(pos: BlockPos, state: BlockState) :
    ShelfBlockEntity(pos, state),
    BlockEntityClientSerializable,
    ExtendedScreenHandlerFactory {
    override fun toClientTag(nbt: NbtCompound) = nbt.apply {
        Inventories.writeNbt(nbt, items)
    }

    override fun fromClientTag(nbt: NbtCompound) {
        Inventories.readNbt(nbt, items)
    }
}
