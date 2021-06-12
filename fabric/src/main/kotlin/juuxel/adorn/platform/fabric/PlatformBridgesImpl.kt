@file:JvmName("PlatformBridgesKtImpl")
package juuxel.adorn.platform.fabric

import juuxel.adorn.config.AdornGameRules
import juuxel.adorn.config.ConfigManager
import juuxel.adorn.platform.GameRuleBridge
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
    object : PlatformBridges {
        override val blocks = BlockBridgeImpl
        override val blockEntities = BlockEntityDescriptorsImpl
        override val config = ConfigManager.CONFIG
        override val entities = EntityBridgeImpl
        override val gameRules = object : GameRuleBridge {
            override val skipNightOnSofas = AdornGameRules.SKIP_NIGHT_ON_SOFAS
        }
        override val items = ItemBridgeImpl
        override val menus = MenuBridgeImpl
        override val network = NetworkBridgeImpl
        override val registrarFactory: RegistrarFactory = object : RegistrarFactory {
            override fun block(): Registrar<Block> {
                return RegistrarImpl(Registry.BLOCK)
            }

            override fun item(): Registrar<Item> {
                return RegistrarImpl(Registry.ITEM)
            }

            override fun blockEntity(): Registrar<BlockEntityType<*>> {
                return RegistrarImpl(Registry.BLOCK_ENTITY_TYPE)
            }

            override fun entity(): Registrar<EntityType<*>> {
                return RegistrarImpl(Registry.ENTITY_TYPE)
            }

            override fun menu(): Registrar<ScreenHandlerType<*>> {
                return RegistrarImpl(Registry.SCREEN_HANDLER)
            }

            override fun soundEvent(): Registrar<SoundEvent> {
                return RegistrarImpl(Registry.SOUND_EVENT)
            }
        }
        override val tags = TagBridgeImpl
    }
