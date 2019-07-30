package juuxel.adorn.compat.extrapieces

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.api.util.BlockVariant
import net.minecraft.block.Block
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

fun PieceSet.toVariant(): BlockVariant =
    object : BlockVariant {
        override val variantName = originalName

        override fun createSettings() = Block.Settings.copy(base)
    }

fun PieceBlock.getRegistryId(): Identifier = Registry.BLOCK.getId(block)
