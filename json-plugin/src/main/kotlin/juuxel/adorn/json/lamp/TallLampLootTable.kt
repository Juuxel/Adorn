package juuxel.adorn.json.lamp

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.loot.Condition
import io.github.cottonmc.jsonfactory.output.loot.Entry
import io.github.cottonmc.jsonfactory.output.loot.LootTable
import io.github.cottonmc.jsonfactory.output.loot.Pool
import io.github.cottonmc.jsonfactory.output.suffixed
import juuxel.adorn.json.AdornPlugin

object TallLampLootTable : AbstractContentGenerator(
    "tall_lamp.loot_table", "loot_tables/blocks",
    AdornPlugin.OTHER, resourceRoot = ResourceRoot.Data
) {
    override fun generate(id: Identifier) = listOf(
        LootTable(
            pools = listOf(
                Pool(
                    entries = listOf(
                        Entry(
                            id.suffixPath("_tall_lamp"),
                            conditions = listOf(
                                Condition.BlockStateProperty(
                                    id.suffixPath("_tall_lamp"),
                                    properties = mapOf("half" to "lower")
                                )
                            )
                        )
                    ),
                    conditions = listOf(
                        Condition(
                            condition = Identifier.mc("survives_explosion")
                        )
                    )
                )
            )
        ).suffixed("tall_lamp")
    )
}
