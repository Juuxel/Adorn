package juuxel.adorn.loot;

import com.mojang.serialization.MapCodec;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public final class AdornLootConditionTypes {
    public static final Registrar<MapCodec<? extends LootItemCondition>> LOOT_CONDITION_TYPES = RegistrarFactory.get().create(Registries.LOOT_CONDITION_TYPE);
    public static final Registered<MapCodec<GameRuleLootCondition>> GAME_RULE =
        LOOT_CONDITION_TYPES.register("game_rule", () -> GameRuleLootCondition.CODEC);

    public static void init() {
    }
}
