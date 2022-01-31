@file:JvmName("PlatformBridgesKtImpl")
package juuxel.adorn.platform.forge

import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import juuxel.adorn.platform.RegistrarFactory
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.menu.MenuType
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.sound.SoundEvent
import net.minecraftforge.registries.ForgeRegistries

fun PlatformBridges.Companion.get(): PlatformBridges =
    PlatformBridgesImpl

private object PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityBridgeForge
    override val blockFactory = BlockFactoryImpl
    override val criteria = CriterionBridgeImpl
    override val configManager = ConfigManagerImpl
    override val entities = EntityBridgeImpl
    override val items = ItemBridgeImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val registrarFactory: RegistrarFactory = object : RegistrarFactory {
        override fun block(): Registrar<Block> = RegistrarImpl(ForgeRegistries.BLOCKS)
        override fun item(): Registrar<Item> = RegistrarImpl(ForgeRegistries.ITEMS)
        override fun blockEntity(): Registrar<BlockEntityType<*>> = RegistrarImpl(ForgeRegistries.BLOCK_ENTITIES)
        override fun entity(): Registrar<EntityType<*>> = RegistrarImpl(ForgeRegistries.ENTITIES)
        override fun menu(): Registrar<MenuType<*>> = RegistrarImpl(ForgeRegistries.CONTAINERS)
        override fun soundEvent(): Registrar<SoundEvent> = RegistrarImpl(ForgeRegistries.SOUND_EVENTS)
        override fun recipeSerializer(): Registrar<RecipeSerializer<*>> = RegistrarImpl(ForgeRegistries.RECIPE_SERIALIZERS)
    }
    override val resources = ResourceBridgeImpl
    override val tags = TagBridgeImpl
}
