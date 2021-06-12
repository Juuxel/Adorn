package juuxel.adorn.platform

import net.minecraft.block.AbstractBlock

interface BlockBridge {
    // Requires pickaxe
    fun createStoneLadderSettings(): AbstractBlock.Settings
}
