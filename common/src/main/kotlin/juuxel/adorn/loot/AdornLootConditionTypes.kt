package juuxel.adorn.loot

import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.util.registry.Registry

object AdornLootConditionTypes {
    val LOOT_CONDITION_TYPES: Registrar<LootConditionType> = PlatformBridges.registrarFactory.create(Registry.LOOT_CONDITION_TYPE_KEY)
    val GAME_RULE: LootConditionType by LOOT_CONDITION_TYPES.register("game_rule") { LootConditionType(GameRuleLootCondition.Serializer) }

    fun init() {
    }
}
