package juuxel.adorn.json.output

import io.github.cottonmc.jsonfactory.output.ExperimentalMapOutput
import io.github.cottonmc.jsonfactory.output.MapJsonOutput

// This is evil and must *never* end up in json-factory
// Edit: it already did
@UseExperimental(ExperimentalMapOutput::class)
typealias MapOutput = MapJsonOutput
