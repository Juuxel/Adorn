package juuxel.adorn.platform

import net.minecraft.world.GameRules

interface GameRuleBridge {
    val skipNightOnSofas: GameRules.Key<GameRules.BooleanRule>
}
