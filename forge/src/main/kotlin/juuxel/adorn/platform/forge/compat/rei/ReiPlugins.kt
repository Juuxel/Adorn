@file:Suppress("UNUSED")

package juuxel.adorn.platform.forge.compat.rei

import juuxel.adorn.compat.rei.AdornReiServer
import juuxel.adorn.compat.rei.client.AdornReiClient
import me.shedaniel.rei.forge.REIPluginClient
import me.shedaniel.rei.forge.REIPluginCommon
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
@REIPluginClient
class AdornReiClientForge : AdornReiClient()

@REIPluginCommon
class AdornReiServerForge : AdornReiServer()
