package juuxel.adorn.util

import com.mojang.serialization.DataResult
import com.mojang.serialization.Dynamic
import com.mojang.serialization.JsonOps
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.datafixer.NbtOps
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.state.property.Property
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.registry.Registry
import java.util.UUID

fun CompoundTag.putText(name: String, textComponent: Text) =
    put(
        name,
        Dynamic.convert(
            JsonOps.INSTANCE,
            NbtOps.INSTANCE,
            Text.Serializer.toJsonTree(textComponent)
        )
    )

fun CompoundTag.getText(name: String): Text? {
    val tag = get(name) ?: return null

    return Text.Serializer.fromJson(
        Dynamic.convert(
            NbtOps.INSTANCE,
            JsonOps.INSTANCE,
            tag
        )
    )
}

fun CompoundTag.containsOldUuid(key: String): Boolean =
    contains("${key}Most") && contains("${key}Least")

fun CompoundTag.getOldUuid(key: String): UUID =
    UUID(getLong("${key}Most"), getLong("${key}Least"))

fun ItemStack.toTextWithCount(): Text =
    TranslatableText("text.adorn.item_stack_with_count", count, toHoverableText())

fun BlockState.withBlock(block: Block): BlockState =
    entries.entries.fold(block.defaultState) { acc, (key, value) ->
        @Suppress("UNCHECKED_CAST") // Cast to Comparable<Any>
        acc.with(key as Property<Comparable<Any>>, value as Comparable<Any>)
    }

/**
 * Gets the squared distance of this block entity to ([x], [y], [z]).
 *
 * Used to be in vanilla but was removed.
 */
fun BlockEntity.getSquaredDistance(x: Double, y: Double, z: Double): Double {
    val xd = pos.x + 0.5 - x
    val yd = pos.y + 0.5 - y
    val zd = pos.z + 0.5 - z
    return xd * xd + yd * yd + zd * zd
}

fun <R> DataResult<R>.orElse(default: R): R = get().map({ it }, { default })

inline fun <A> Registry<A>.visit(crossinline visitor: (A) -> Unit) {
    this.forEach(visitor)

    RegistryEntryAddedCallback.event(this)
        .register(
            RegistryEntryAddedCallback { rawId, id, entry ->
                visitor(entry)
            }
        )
}

/**
 * Creates a safe copy of this block's settings.
 *
 * The safe copy does not have lambdas that reference this block directly.
 * Instead, the default state is used for the various lambdas.
 */
fun Block.copySettingsSafely(): AbstractBlock.Settings =
    FabricBlockSettings.of(defaultState.material)
        .lightLevel(defaultState.luminance)
        .apply { getHardness(defaultState)?.let(this::hardness) }
        .resistance(blastResistance)
        .velocityMultiplier(velocityMultiplier)
        .jumpVelocityMultiplier(jumpVelocityMultiplier)
        .slipperiness(slipperiness)
        .sounds(defaultState.soundGroup)

private fun getHardness(state: BlockState): Float? =
    try {
        state.getHardness(null, null)
    } catch (e: NullPointerException) {
        null
    }
