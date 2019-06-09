package juuxel.adorn.json

import io.github.cottonmc.jsonfactory.data.Identifier
import io.github.cottonmc.jsonfactory.gens.AbstractContentGenerator
import io.github.cottonmc.jsonfactory.gens.GeneratorInfo
import io.github.cottonmc.jsonfactory.gens.ResourceRoot
import io.github.cottonmc.jsonfactory.output.Property
import io.github.cottonmc.jsonfactory.output.loot.Condition
import io.github.cottonmc.jsonfactory.output.loot.Entry
import io.github.cottonmc.jsonfactory.output.loot.Function
import io.github.cottonmc.jsonfactory.output.loot.LootTable
import io.github.cottonmc.jsonfactory.output.loot.Pool
import io.github.cottonmc.jsonfactory.output.suffixed

class SuffixedContainerLootTable(private val type: String, info: GeneratorInfo) :
    AbstractContentGenerator("$type.loot_table", "loot_tables/blocks", info, resourceRoot = ResourceRoot.Data) {
    override fun generate(id: Identifier) = listOf(
        LootTable(
            pools = listOf(
                Pool(
                    entries = listOf(
                        Entry(
                            id.suffixPath("_$type"),
                            functions = listOf(
                                Function(Identifier.mc("copy_name"), additionalProperties = listOf(
                                    Property("source", "block_entity")
                                ))
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
        ).suffixed(type)
    )
}
