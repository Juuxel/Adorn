package juuxel.adorn.block.entity

import io.github.juuxel.polyester.block.PolyesterBlockEntityType
import juuxel.adorn.block.PossiblyCarpetedBlock
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.DyeColor
import net.minecraft.world.loot.context.LootContext

class CarpetedBlockEntity : BlockEntity(BLOCK_ENTITY_TYPE) {
    var carpet: DyeColor? = null
        set(value) {
            field = value
            carpetState = value?.let(PossiblyCarpetedBlock.COLORS_TO_BLOCKS::getValue)?.defaultState ?: Blocks.AIR.defaultState
        }

    var carpetState: BlockState = Blocks.AIR.defaultState
        private set

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        tag.putInt("Carpet", carpet?.id ?: -1)
    }

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)
        val carpetValue = tag.getInt("Carpet")
        carpet = if (carpetValue == -1) null else DyeColor.byId(carpetValue)
    }

    fun getDroppedStacks(builder: LootContext.Builder) = carpetState.getDroppedStacks(builder)

    companion object {
        val BLOCK_ENTITY_TYPE = PolyesterBlockEntityType(::CarpetedBlockEntity)
    }
}
