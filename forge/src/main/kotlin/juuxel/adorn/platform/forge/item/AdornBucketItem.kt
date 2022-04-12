package juuxel.adorn.platform.forge.item

import juuxel.adorn.item.AdornItems
import net.minecraft.fluid.Fluid
import net.minecraft.item.BucketItem
import net.minecraft.item.ItemGroup

class AdornBucketItem(fluid: () -> Fluid, settings: Settings) : BucketItem(fluid, settings) {
    override fun isIn(group: ItemGroup?): Boolean =
        AdornItems.isIn(group, this)
}
