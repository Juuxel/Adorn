package juuxel.adorn.util;

import net.minecraft.world.GameRules;

// To avoid issues with generics caused by Kotlin
public class JavaUtils {
    public static GameRules.Rule<?> getGameRule(GameRules rules, GameRules.RuleKey<?> key) {
        return rules.get(key);
    }
}
