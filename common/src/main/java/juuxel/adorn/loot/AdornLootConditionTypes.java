package juuxel.adorn.loot;

import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.core.registries.Registries;

public final class AdornLootConditionTypes {
    public static final Registrar<LootItemConditionType> LOOT_CONDITION_TYPES = RegistrarFactory.get().create(Registries.LOOT_CONDITION_TYPE);
    public static final Registered<LootItemConditionType> GAME_RULE =
        LOOT_CONDITION_TYPES.register("game_rule", () -> new LootItemConditionType(GameRuleLootCondition.CODEC));

    public static void init() {
    }
}
