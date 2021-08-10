package juuxel.adorn.platform.forge

import juuxel.adorn.platform.Registrar
import net.minecraftforge.eventbus.api.IEventBus

fun Registrar<*>.register(modBus: IEventBus): Unit =
    (this as RegistrarImpl).register.register(modBus)
