package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.item.StoneRodItem
import net.minecraft.util.registry.Registry

object ModItems : PolyesterRegistry(Adorn.NAMESPACE) {
    val STONE_ROD = register(Registry.ITEM, StoneRodItem())

    fun init() {}
}
