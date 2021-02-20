package juuxel.adorn.compat

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModList

object Compat {
    fun init(modBus: IEventBus) {
        loadCompat("byg") { BygCompat.init(modBus) }
        loadCompat("traverse") { TraverseCompat.init(modBus) }
        loadCompat("biomesoplenty") { BopCompat.init(modBus) }
    }

    private inline fun loadCompat(mod: String, compatInit: () -> Unit) {
        if (ModList.get().isLoaded(mod)) {
            compatInit()
        }
    }
}
