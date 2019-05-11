package juuxel.adorn.patcher.patches

import com.mojang.datafixers.Dynamic
import net.minecraft.nbt.Tag

abstract class Patch(val from: Short, val to: Short) {
    init {
        require(from < to) {
            "from must be less than to"
        }
    }

    abstract fun patch(dynamic: Dynamic<Tag>): Dynamic<Tag>

    fun andThen(other: Patch): Patch {
        require(from < other.from && other.from == to)
        val self = this

        return object : Patch(from, other.to) {
            override fun patch(dynamic: Dynamic<Tag>) = other.patch(self.patch(dynamic))
        }
    }
}
