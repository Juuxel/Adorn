package juuxel.adorn.util

import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.JsonOps
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.datafixers.NbtOps
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.state.property.Property
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import java.util.Optional

fun CompoundTag.putTextComponent(name: String, textComponent: Text) =
    put(
        name,
        Dynamic.convert(
            JsonOps.INSTANCE,
            NbtOps.INSTANCE,
            Text.Serializer.toJsonTree(textComponent)
        )
    )

fun CompoundTag.getTextComponent(name: String): Text? {
    val tag = get(name) ?: return null

    return Text.Serializer.fromJson(
        Dynamic.convert(
            NbtOps.INSTANCE,
            JsonOps.INSTANCE,
            tag
        )
    )
}

fun ItemStack.toTextWithCount(): Text =
    TranslatableText("text.adorn.item_stack_with_count", count, toHoverableText())

fun BlockState.withBlock(block: Block): BlockState =
    entries.entries.fold(block.defaultState) { acc, (key, value) ->
        @Suppress("UNCHECKED_CAST") // Cast to Comparable<Any>
        acc.with(key as Property<Comparable<Any>>, value as Comparable<Any>)
    }
