package juuxel.adorn.platform.forge

import juuxel.adorn.platform.TagBridge
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.BlockTags
import net.minecraft.tag.ItemTags
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

object TagBridgeImpl : TagBridge {
    override fun block(id: Identifier): Tag<Block> = BlockTags.createOptional(id)
    override fun item(id: Identifier): Tag<Item> = ItemTags.createOptional(id)
}
