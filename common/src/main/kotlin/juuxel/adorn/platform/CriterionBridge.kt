package juuxel.adorn.platform

import net.minecraft.advancement.criterion.Criterion

interface CriterionBridge {
    fun <T : Criterion<*>> register(criterion: T): T
}
