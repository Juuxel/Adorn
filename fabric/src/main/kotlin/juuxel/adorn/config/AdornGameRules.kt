package juuxel.adorn.config

import juuxel.adorn.AdornCommon
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory.createBooleanRule
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry
import net.minecraft.world.GameRules.BooleanRule
import net.minecraft.world.GameRules.Category
import net.minecraft.world.GameRules.Key
import net.minecraft.world.GameRules.Rule
import net.minecraft.world.GameRules.Type

object AdornGameRules {
    @JvmField
    val SKIP_NIGHT_ON_SOFAS: Key<BooleanRule> = register("skipNightOnSofas", Category.PLAYER, createBooleanRule(true))

    fun init() {
    }

    // <T extends Rule<T>> Key<T> register(String name, Category category, Type<T> type)
    private fun <T : Rule<T>> register(name: String, category: Category, type: Type<T>): Key<T> =
        GameRuleRegistry.register("${AdornCommon.NAMESPACE}:$name", category, type)
}
