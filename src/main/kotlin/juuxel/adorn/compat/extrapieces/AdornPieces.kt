package juuxel.adorn.compat.extrapieces

import com.shnupbups.extrapieces.api.EPInitializer
import com.shnupbups.extrapieces.core.PieceSet
import com.shnupbups.extrapieces.core.PieceSets
import com.shnupbups.extrapieces.core.PieceType
import com.shnupbups.extrapieces.core.PieceTypes
import com.swordglowsblue.artifice.api.ArtificeResourcePack
import juuxel.adorn.Adorn
import juuxel.adorn.block.entity.BETypeProvider
import juuxel.adorn.block.entity.MutableBlockEntityType
import juuxel.adorn.compat.extrapieces.piece.ChairPiece
import juuxel.adorn.compat.extrapieces.piece.DrawerPiece
import juuxel.adorn.compat.extrapieces.piece.KitchenCounterPiece
import juuxel.adorn.compat.extrapieces.piece.KitchenCupboardPiece
import juuxel.adorn.compat.extrapieces.piece.PlatformPiece
import juuxel.adorn.compat.extrapieces.piece.PostPiece
import juuxel.adorn.compat.extrapieces.piece.ShelfPiece
import juuxel.adorn.compat.extrapieces.piece.SofaPiece
import juuxel.adorn.compat.extrapieces.piece.StepPiece
import juuxel.adorn.compat.extrapieces.piece.TablePiece
import juuxel.adorn.config.ConfigManager
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback
import net.minecraft.block.Block
import net.minecraft.item.Items
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry

object AdornPieces : EPInitializer {
    val HORIZONTAL_DIRECTIONS = Direction.values().filter { it.axis.isHorizontal }

    val DRAWER = DrawerPiece
    val TABLE = TablePiece
    val SHELF = ShelfPiece
    val POST = PostPiece
    val PLATFORM = PlatformPiece
    val STEP = StepPiece
    val CHAIR = ChairPiece
    val SOFA = SofaPiece
    val KITCHEN_COUNTER = KitchenCounterPiece
    val KITCHEN_CUPBOARD = KitchenCupboardPiece

    private val carpetedSets: MutableMap<PieceSet, Boolean> = HashMap()

    override fun addData(data: ArtificeResourcePack.ServerResourcePackBuilder) {
        if (!ConfigManager.CONFIG.extraPieces.enabled) return
        PieceSets.registry.values.forEach { set ->
            if (set.hasPiece(KITCHEN_COUNTER) && set.hasPiece(KITCHEN_CUPBOARD)) {
                val id = Registry.BLOCK.getId(set.getPiece(KITCHEN_CUPBOARD))
                val counter = Registry.ITEM.getId(set.getPiece(KITCHEN_COUNTER).asItem())
                data.addShapelessRecipe(id) {
                    it.result(id, 2)
                    it.group(Adorn.id(set.originalName))

                    it.ingredientItem(Registry.ITEM.getId(Items.CHEST))
                    it.ingredientItem(counter)
                    it.ingredientItem(counter)
                }
            }
        }
    }

    override fun onInitialize() {
        if (!ConfigManager.CONFIG.extraPieces.enabled) return
        register(DRAWER, TABLE, SHELF, POST, PLATFORM, STEP, CHAIR, SOFA, KITCHEN_COUNTER, KITCHEN_CUPBOARD)

        RegistryEntryAddedCallback.event(Registry.BLOCK).register(
            RegistryEntryAddedCallback { _, _, block: Block ->
                if (block is BETypeProvider) {
                    (block.blockEntityType as? MutableBlockEntityType<*>)?.addBlock(block)
                }
            }
        )
    }

    fun isCarpetingEnabled(set: PieceSet): Boolean =
        ConfigManager.CONFIG.extraPieces.carpetedEverything ||
            carpetedSets.getOrPut(set) {
                set.originalName in ConfigManager.CONFIG.extraPieces.carpetedPieceSets
            }

    private fun register(vararg types: PieceType) {
        for (type in types) {
            PieceTypes.register(type)
            // ModItemGroups.groups[type] = ItemGroup.DECORATIONS
        }
    }

    override fun toString() = "from Adorn is wondering why Extra Pieces is using toString()"
}
