package juuxel.adorn.lib

import io.netty.buffer.Unpooled
import juuxel.adorn.mixin.gamerule.BooleanRuleAccessor
import juuxel.adorn.mixin.gamerule.GameRulesAccessor
import juuxel.adorn.mixin.gamerule.RuleAccessor
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.server.MinecraftServer
import net.minecraft.util.PacketByteBuf
import net.minecraft.world.GameRules.*

object AdornGameRules {
    @JvmField val SKIP_NIGHT_ON_SOFAS = register("adorn:skipNightOnSofas", boolean(true))
    @JvmField val PROTECT_TRADING_STATIONS = register(
        "adorn:protectTradingStations",
        notifyingBoolean(true, "adorn:protectTradingStations")
    )

    private fun <T : Rule<T>> register(name: String, type: RuleType<T>): RuleKey<T> =
        GameRulesAccessor.callRegister(name, type)

    private fun boolean(default: Boolean): RuleType<BooleanRule> =
        BooleanRuleAccessor.callOf(default)

    private fun notifyingBoolean(default: Boolean, name: String): RuleType<BooleanRule> =
        boolean(default) { server, rule ->
            PlayerStream.all(server).forEach {
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(
                    it, AdornNetworking.GAME_RULE, PacketByteBuf(Unpooled.buffer()).also { buf ->
                        buf.writeString(name)
                        buf.writeString((rule as RuleAccessor).callValueToString())
                    }
                )
            }
        }

    private inline fun boolean(default: Boolean, crossinline notifier: (MinecraftServer, BooleanRule) -> Unit): RuleType<BooleanRule> =
        BooleanRuleAccessor.callOf(default) { a, b -> notifier(a, b) }

    @JvmStatic fun init() {}
}
