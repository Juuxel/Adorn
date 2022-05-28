package juuxel.adorn.lib

import juuxel.adorn.AdornCommon
import juuxel.adorn.platform.PlatformBridges
import juuxel.adorn.platform.Registrar
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

object AdornSounds {
    @JvmField
    val SOUNDS: Registrar<SoundEvent> = PlatformBridges.registrarFactory.create(Registry.SOUND_EVENT_KEY)

    val BLOCK_CHAIN_LINK_FENCE_BREAK = register("block.adorn.chain_link_fence.break")
    val BLOCK_CHAIN_LINK_FENCE_STEP = register("block.adorn.chain_link_fence.step")
    val BLOCK_CHAIN_LINK_FENCE_PLACE = register("block.adorn.chain_link_fence.place")
    val BLOCK_CHAIN_LINK_FENCE_HIT = register("block.adorn.chain_link_fence.hit")
    val BLOCK_CHAIN_LINK_FENCE_FALL = register("block.adorn.chain_link_fence.fall")

    val CHAIN_LINK_FENCE: BlockSoundGroup = LazyBlockSoundGroup(
        1.0F,
        1.5F,
        BLOCK_CHAIN_LINK_FENCE_BREAK::get,
        BLOCK_CHAIN_LINK_FENCE_STEP::get,
        BLOCK_CHAIN_LINK_FENCE_PLACE::get,
        BLOCK_CHAIN_LINK_FENCE_HIT::get,
        BLOCK_CHAIN_LINK_FENCE_FALL::get
    )

    fun init() {}

    private fun register(name: String): Registered<SoundEvent> =
        SOUNDS.register(name) { SoundEvent(AdornCommon.id(name)) }

    private class LazyBlockSoundGroup(
        volume: Float, pitch: Float,
        private val breakSound: () -> SoundEvent,
        private val stepSound: () -> SoundEvent,
        private val placeSound: () -> SoundEvent,
        private val hitSound: () -> SoundEvent,
        private val fallSound: () -> SoundEvent
    ) : BlockSoundGroup(volume, pitch, null, null, null, null, null) {
        override fun getBreakSound() = breakSound()
        override fun getStepSound() = stepSound()
        override fun getPlaceSound() = placeSound()
        override fun getHitSound() = hitSound()
        override fun getFallSound() = fallSound()
    }
}
