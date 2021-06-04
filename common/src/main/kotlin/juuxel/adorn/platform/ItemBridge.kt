package juuxel.adorn.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup

object ItemBridge {
    @JvmStatic
    @ExpectPlatform
    fun createAdornItemGroup(): ItemGroup = expected

    @JvmStatic
    @ExpectPlatform
    fun createGuideBook(): Item? = expected

    @JvmStatic
    @ExpectPlatform
    fun createTradersManual(): Item? = expected
}
