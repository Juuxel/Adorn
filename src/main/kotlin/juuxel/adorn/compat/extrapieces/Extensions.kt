package juuxel.adorn.compat.extrapieces

import com.shnupbups.extrapieces.blocks.PieceBlock
import com.shnupbups.extrapieces.core.PieceSet
import juuxel.adorn.api.block.BlockVariant
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

fun PieceSet.toVariant(): BlockVariant =
    object : BlockVariant {
        override val name = originalName

        override fun createSettings() = FabricBlockSettings.copyOf(base)
    }

fun PieceBlock.getRegistryId(): Identifier = Registry.BLOCK.getId(block)
