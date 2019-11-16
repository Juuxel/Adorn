package juuxel.adorn.part

import alexiil.mc.lib.multipart.api.AbstractPart
import alexiil.mc.lib.multipart.api.MultipartHolder
import alexiil.mc.lib.multipart.api.PartDefinition
import alexiil.mc.lib.net.IMsgWriteCtx
import alexiil.mc.lib.net.NetByteBuf
import juuxel.adorn.block.TableBlock
import juuxel.blockstoparts.model.DynamicVanillaModelKey
import juuxel.blockstoparts.part.BasePart
import juuxel.blockstoparts.part.Categories
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.EmptyBlockView

class TablePart(
    def: PartDefinition,
    holder: MultipartHolder,
    private val table: Block,
    private var north: Boolean = false,
    private var east: Boolean = false,
    private var south: Boolean = false,
    private var west: Boolean = false
) : BasePart(def, holder) {
    override fun getBlockState(): BlockState = table.defaultState
            .with(TableBlock.NORTH, north)
            .with(TableBlock.EAST, east)
            .with(TableBlock.SOUTH, south)
            .with(TableBlock.WEST, west)

    override fun getModelKey() = DynamicVanillaModelKey(this)

    override fun getShape() = table.defaultState.getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN)
    override fun getDynamicShape(partialTicks: Float) = blockState.getOutlineShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN)
    override fun getCollisionShape() = blockState.getCollisionShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN)

    override fun getCategories() = CATEGORIES

    override fun toTag(): CompoundTag = super.toTag().apply {
        putString("Table", Registry.BLOCK.getId(table).toString())
        putBoolean("North", north)
        putBoolean("East", east)
        putBoolean("South", south)
        putBoolean("West", west)
    }

    override fun writeCreationData(buf: NetByteBuf, ctx: IMsgWriteCtx) {
        super.writeCreationData(buf, ctx)
        buf.writeVarInt(Registry.BLOCK.getRawId(table))
            .writeBoolean(north)
            .writeBoolean(east)
            .writeBoolean(south)
            .writeBoolean(west)
    }

    companion object {
        private val CATEGORIES = Categories.builder()
            .add("table")
            .overlap("carpet")
            .build()
    }
}
