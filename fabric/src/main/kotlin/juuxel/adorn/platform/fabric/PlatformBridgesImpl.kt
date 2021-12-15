@file:JvmName("PlatformBridgesKtImpl")
package juuxel.adorn.platform.fabric

import juuxel.adorn.platform.BlockFactory
import juuxel.adorn.platform.KitchenSinkEjection
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import juuxel.adorn.platform.RegistrarFactory
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

fun PlatformBridges.Companion.get(): PlatformBridges =
    PlatformBridgesImpl

private object PlatformBridgesImpl : PlatformBridges {
    override val blockEntities = BlockEntityDescriptorsImpl
    override val blockFactory = BlockFactory
    override val configManager = ConfigManagerImpl
    override val entities = EntityBridgeImpl
    override val items = ItemBridgeImpl
    override val kitchenSinkEjection = KitchenSinkEjectionImpl
    override val menus = MenuBridgeImpl
    override val network = NetworkBridgeImpl
    override val registrarFactory: RegistrarFactory = object : RegistrarFactory {
        override fun block(): Registrar<Block> = RegistrarImpl(Registry.BLOCK)
        override fun item(): Registrar<Item> = RegistrarImpl(Registry.ITEM)
        override fun blockEntity(): Registrar<BlockEntityType<*>> = RegistrarImpl(Registry.BLOCK_ENTITY_TYPE)
        override fun entity(): Registrar<EntityType<*>> = RegistrarImpl(Registry.ENTITY_TYPE)
        override fun menu(): Registrar<ScreenHandlerType<*>> = RegistrarImpl(Registry.SCREEN_HANDLER)
        override fun soundEvent(): Registrar<SoundEvent> = RegistrarImpl(Registry.SOUND_EVENT)
    }
    override val tags = TagBridgeImpl
}
