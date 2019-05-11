package juuxel.adorn.patcher

import com.mojang.datafixers.Dynamic
import juuxel.adorn.patcher.patches.Patch
import net.minecraft.nbt.Tag

class Patcher {
    private val patches = ArrayList<Patch>()

    fun addPatch(patch: Patch) {
        patches += patch
    }

    fun patch(value: Dynamic<Tag>, from: Short, to: Short): Dynamic<Tag> {
        val appliedPatches = patches.asSequence().filter { it.from >= from && it.to <= to }.sortedBy { it.from }
        return if (appliedPatches.none()) value
            else appliedPatches.fold(value) { acc, patch -> patch.patch(acc) }
    }

    companion object {
        fun createFor(patchProvider: PatchProvider) = Patcher().apply {
            patchProvider.patches.forEach(::addPatch)
        }
    }
}
