package juuxel.adorn.patcher.patches

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.util.Pair
import juuxel.adorn.util.component1
import juuxel.adorn.util.component2
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import kotlin.streams.asStream

class Lba2AdornInventoryPatch : Patch(0, 1) {
    override fun patch(dynamic: Dynamic<Tag>) = dynamic.updateMapValues { pair ->
        val ops = dynamic.ops
        val (key, value) = pair

        if (key.asString().orElse("") == "slots") {
            Pair(
                dynamic.createString("Items"),
                Dynamic(
                    ops, ops.createList(
                    value.asList {
                        it.convert(ops).value as CompoundTag
                    }.asSequence().mapIndexed { i, tag ->
                        tag.apply {
                            putByte("Slot", i.toByte())
                        }
                    }.asStream()
                ))
            )
        } else {
            pair
        }
    }
}
