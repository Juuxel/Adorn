package juuxel.adorn.lib

import io.github.juuxel.polyester.registry.PolyesterRegistry
import juuxel.adorn.Adorn
import juuxel.adorn.block.*
import juuxel.adorn.util.VanillaWoodType
import net.fabricmc.fabric.api.registry.CommandRegistry
import net.minecraft.command.arguments.GameProfileArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.text.StringTextComponent
import net.minecraft.util.DyeColor
import net.minecraft.util.registry.Registry

object ModBlocks : PolyesterRegistry(Adorn.NAMESPACE) {
    val SOFAS: List<SofaBlock> = DyeColor.values().map {
        registerBlock(SofaBlock(it.getName()))
    }

    val CHAIRS: List<ChairBlock> = VanillaWoodType.values().map {
        registerBlock(ChairBlock(it.id))
    }

    val TABLES: List<TableBlock> = VanillaWoodType.values().map {
        registerBlock(TableBlock(it.id))
    }

    val KITCHEN_COUNTERS: List<KitchenCounterBlock> = VanillaWoodType.values().map {
        registerBlock(KitchenCounterBlock(it.id))
    }

    val KITCHEN_CUPBOARDS: List<KitchenCupboardBlock> = VanillaWoodType.values().map {
        registerBlock(KitchenCupboardBlock(it.id))
    }

    val CHIMNEY: ChimneyBlock = registerBlock(ChimneyBlock())

    val DRAWERS: List<DrawerBlock> = VanillaWoodType.values().map {
        registerBlock(DrawerBlock(it.id))
    }

    val TRADING_TABLE: TradingTableBlock = registerBlock(TradingTableBlock())

    fun init() {
        // Register here so they're only registered once
        register(Registry.BLOCK_ENTITY, "kitchen_cupboard", KitchenCupboardBlock.BLOCK_ENTITY_TYPE)
        register(Registry.BLOCK_ENTITY, "drawer", DrawerBlock.BLOCK_ENTITY_TYPE)

        CommandRegistry.INSTANCE.register(false) { dispatcher ->
            dispatcher.register(CommandManager.literal("adorn-test").then(
                CommandManager.argument("player", GameProfileArgumentType.create()).executes {
                    it.source.sendFeedback(
                        StringTextComponent(it.getArgument("player", GameProfileArgumentType.GameProfileArgument::class.java).getNames(it.source).joinToString()), false
                    )
                    1
                }
            ))
        }
    }
}
