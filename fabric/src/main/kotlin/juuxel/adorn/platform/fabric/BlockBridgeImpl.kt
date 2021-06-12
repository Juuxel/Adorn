package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.BlockBridge
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Blocks

object BlockBridgeImpl : BlockBridge {
    override fun createStoneLadderSettings(): AbstractBlock.Settings =
        FabricBlockSettings.copyOf(Blocks.STONE)
            .breakByTool(FabricToolTags.PICKAXES)
            .nonOpaque()
}
