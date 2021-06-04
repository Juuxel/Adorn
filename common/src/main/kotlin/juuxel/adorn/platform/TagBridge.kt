package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier

object TagBridge {
    @JvmStatic
    @ExpectPlatform
    fun block(id: Identifier): Tag<Block> = expected

    @JvmStatic
    @ExpectPlatform
    fun item(id: Identifier): Tag<Item> = expected
}
