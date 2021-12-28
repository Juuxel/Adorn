package juuxel.adorn.platform.forge

import juuxel.adorn.platform.CriterionBridge
import net.minecraft.advancement.criterion.Criteria
import net.minecraft.advancement.criterion.Criterion

object CriterionBridgeImpl : CriterionBridge {
    override fun <T : Criterion<*>> register(criterion: T): T =
        Criteria.register(criterion)
}
