@file:Suppress("UNUSED")
package juuxel.adorn.platform.forge.compat.rei

import juuxel.adorn.compat.rei.AdornReiServer
import juuxel.adorn.compat.rei.client.AdornReiClient
import me.shedaniel.rei.forge.REIPlugin
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
@REIPlugin(Dist.CLIENT)
class AdornReiClientForge : AdornReiClient()

@REIPlugin
class AdornReiServerForge : AdornReiServer()
