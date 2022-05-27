package juuxel.adorn.platform.forge

import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import juuxel.adorn.platform.RegistrarFactory
import juuxel.adorn.platform.forge.client.FluidRenderingBridgeForge
import juuxel.adorn.platform.forge.registrar.ForgeRegistryRegistrar
import juuxel.adorn.platform.forge.registrar.WelcomeToHell
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.menu.MenuType
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry
import net.minecraftforge.registries.ForgeRegistries

class PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityBridgeForge
    override val blockFactory = BlockFactoryImpl
    override val configManager = ConfigManagerImpl
    override val entities = EntityBridgeImpl
    override val fluids = FluidBridgeForge
    override val fluidRendering = FluidRenderingBridgeForge
    override val items = ItemBridgeImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val registrarFactory: RegistrarFactory = object : RegistrarFactory {
        override fun block(): Registrar<Block> = ForgeRegistryRegistrar(ForgeRegistries.BLOCKS)
        override fun item(): Registrar<Item> = ForgeRegistryRegistrar(ForgeRegistries.ITEMS)
        override fun blockEntity(): Registrar<BlockEntityType<*>> = ForgeRegistryRegistrar(ForgeRegistries.BLOCK_ENTITIES)
        override fun entity(): Registrar<EntityType<*>> = ForgeRegistryRegistrar(ForgeRegistries.ENTITIES)
        override fun menu(): Registrar<MenuType<*>> = ForgeRegistryRegistrar(ForgeRegistries.CONTAINERS)
        override fun soundEvent(): Registrar<SoundEvent> = ForgeRegistryRegistrar(ForgeRegistries.SOUND_EVENTS)
        override fun recipeSerializer(): Registrar<RecipeSerializer<*>> = ForgeRegistryRegistrar(ForgeRegistries.RECIPE_SERIALIZERS)
        override fun recipeType(): Registrar<RecipeType<*>> = WelcomeToHell(Registry.RECIPE_TYPE)
        override fun lootConditionType(): Registrar<LootConditionType> = WelcomeToHell(Registry.LOOT_CONDITION_TYPE)
    }
    override val resources = ResourceBridgeImpl
}
