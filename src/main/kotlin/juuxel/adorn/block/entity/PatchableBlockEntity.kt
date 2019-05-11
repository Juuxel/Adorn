package juuxel.adorn.block.entity

import com.mojang.datafixers.Dynamic
import juuxel.adorn.patcher.Constants
import juuxel.adorn.patcher.Constants.CURRENT_FORMAT
import juuxel.adorn.patcher.PatchProvider
import juuxel.adorn.patcher.Patcher
import juuxel.adorn.patcher.patches.Patch
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.datafixers.NbtOps
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager

abstract class PatchableBlockEntity(type: BlockEntityType<*>) : BlockEntity(type), PatchProvider {
    override val patches: List<Patch> = emptyList()

    override fun fromTag(tag: CompoundTag) {
        super.fromTag(tag)

        val format =
            if (tag.containsKey(Constants.NBT_FORMAT_KEY, NbtType.SHORT)) tag.getShort(Constants.NBT_FORMAT_KEY)
            else 0

        if (format < CURRENT_FORMAT) {
            val patched = Patcher.createFor(this).patch(Dynamic(NbtOps.INSTANCE, tag), format, CURRENT_FORMAT)
            val patchedTag = patched.value as CompoundTag
            fromTag(
                patchedTag.apply {
                    putShort(Constants.NBT_FORMAT_KEY, CURRENT_FORMAT)
                }
            )
            markDirty()
        } else if (format > CURRENT_FORMAT) {
            LOGGER.warn(
                "Future format found in {}: current = {}, found = {}",
                Registry.BLOCK_ENTITY.getId(type),
                CURRENT_FORMAT,
                format
            )
        }
    }

    override fun toTag(tag: CompoundTag) = super.toTag(tag).apply {
        putShort(Constants.NBT_FORMAT_KEY, CURRENT_FORMAT)
    }

    companion object {
        private val LOGGER = LogManager.getLogger()
    }
}
