package juuxel.adorn.compat.extrapieces

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.api.block.BlockVariant
import juuxel.adorn.util.copySettingsSafely
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

fun PieceSet.toVariant(): BlockVariant =
    object : BlockVariant {
        override val name = originalName

        override fun createSettings() = base.copySettingsSafely()
    }

fun PieceBlock.getRegistryId(): Identifier = Registry.BLOCK.getId(block)
