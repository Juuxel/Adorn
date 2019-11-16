package juuxel.adorn.part

import alexiil.mc.lib.multipart.api.AbstractPart
import alexiil.mc.lib.multipart.api.MultipartContainer
import alexiil.mc.lib.multipart.api.MultipartHolder
import alexiil.mc.lib.multipart.api.PartDefinition
import alexiil.mc.lib.net.IMsgReadCtx
import alexiil.mc.lib.net.NetByteBuf
import juuxel.adorn.Adorn
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object AdornParts {
    val TABLE = register(
        "table",
        nbtReader = { def, holder, tag ->
            TablePart(
                def, holder,
                table = Registry.BLOCK[Identifier(tag.getString("Table"))],
                north = tag.getBoolean("North"),
                east = tag.getBoolean("East"),
                south = tag.getBoolean("South"),
                west = tag.getBoolean("West")
            )
        },
        netLoader = { def, holder, buf, _ ->
            TablePart(
                def, holder,
                table = Registry.BLOCK[buf.readVarInt()],
                north = buf.readBoolean(),
                east = buf.readBoolean(),
                south = buf.readBoolean(),
                west = buf.readBoolean()
            )
        }
    )

    fun init() {
        // Dummy
    }

    private inline fun register(
        id: String,
        crossinline nbtReader: (def: PartDefinition, holder: MultipartHolder, tag: CompoundTag) -> AbstractPart,
        crossinline netLoader: (def: PartDefinition, holder: MultipartHolder, buf: NetByteBuf, ctx: IMsgReadCtx) -> AbstractPart
    ): PartDefinition {
        val def = PartDefinition(
            Adorn.id(id),
            PartDefinition.IPartNbtReader { def, holder, tag -> nbtReader(def, holder, tag) },
            PartDefinition.IPartNetLoader { def, holder, buf, ctx -> netLoader(def, holder, buf, ctx) }
        )
        PartDefinition.PARTS[def.identifier] = def
        return def
    }
}

inline fun creator(crossinline fn: (holder: MultipartHolder) -> AbstractPart): MultipartContainer.MultipartCreator =
    MultipartContainer.MultipartCreator { fn(it) }
