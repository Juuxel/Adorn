package juuxel.adorn.computer.clay

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import java.util.Optional

data class Descriptor(val module: String?, val function: String) {
    private constructor(module: Optional<String>, function: String) :
        this(module.orElse(null), function)

    companion object {
        val CODEC: Codec<Descriptor> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.STRING.optionalFieldOf("module").forGetter { Optional.ofNullable(it.module) },
                Codec.STRING.fieldOf("function").forGetter(Descriptor::function),
            ).apply(instance, ::Descriptor)
        }
    }
}
