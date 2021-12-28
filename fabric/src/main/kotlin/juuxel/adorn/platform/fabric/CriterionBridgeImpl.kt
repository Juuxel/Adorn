package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.CriterionBridge
import net.fabricmc.fabric.api.`object`.builder.v1.advancement.CriterionRegistry
import net.minecraft.advancement.criterion.Criterion

object CriterionBridgeImpl : CriterionBridge {
    override fun <T : Criterion<*>> register(criterion: T): T =
        CriterionRegistry.register(criterion)
}
