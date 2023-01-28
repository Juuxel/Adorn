package juuxel.adorn.block.entity

import juuxel.adorn.block.AdornBlockEntities
import juuxel.adorn.block.variant.BlockVariant
import juuxel.adorn.design.FurniturePart
import juuxel.adorn.design.FurniturePartMaterial
import juuxel.adorn.menu.FurnitureWorkbenchMenu
import juuxel.adorn.util.menuContextOf
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.menu.Menu
import net.minecraft.menu.NamedMenuFactory
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import org.joml.Vector3d

class FurnitureWorkbenchBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(AdornBlockEntities.FURNITURE_WORKBENCH, pos, state), NamedMenuFactory {
    var furnitureParts: MutableList<FurniturePart> = ArrayList()

    init {
        furnitureParts += FurniturePart(Vector3d(8.0, 8.0, 8.0), 4, 4, 4, FurniturePartMaterial.of(BlockVariant.OAK))
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): Menu =
        FurnitureWorkbenchMenu(syncId, inv, menuContextOf(this))

    override fun getDisplayName(): Text =
        cachedState.block.name

    override fun readNbt(nbt: NbtCompound) {
        FurniturePart.fromNbt(nbt, NBT_FURNITURE_PARTS)?.let { furnitureParts = ArrayList(it) }
    }

    override fun writeNbt(nbt: NbtCompound) {
        FurniturePart.writeNbt(nbt, NBT_FURNITURE_PARTS, furnitureParts)
    }

    companion object {
        private const val NBT_FURNITURE_PARTS = "FurnitureParts"
    }
}
