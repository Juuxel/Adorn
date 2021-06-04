package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.TagBridge
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

object TagBridgeImpl : TagBridge {
    override fun block(id: Identifier): Tag<Block> =
        TagRegistry.block(id)

    override fun item(id: Identifier): Tag<Item> =
        TagRegistry.item(id)
}
