package juuxel.adorn.patcher

import juuxel.adorn.patcher.patches.Patch

interface PatchProvider {
    val patches: List<Patch>
}
