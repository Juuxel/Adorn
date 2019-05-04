package juuxel.adorn.util.client

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.DrawableHelper

@Environment(EnvType.CLIENT)
object DrawableHelperInstance : DrawableHelper()
