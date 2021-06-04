package juuxel.adorn.platform.fabric

import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

object TagBridgeImpl {
    @JvmStatic
    fun block(id: Identifier): Tag<Block> {
        return TagRegistry.block(id)
    }

    @JvmStatic
    fun item(id: Identifier): Tag<Item> {
        return TagRegistry.item(id)
    }
}
