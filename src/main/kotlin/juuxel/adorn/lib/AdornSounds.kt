package juuxel.adorn.lib

import juuxel.adorn.Adorn
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

object AdornSounds {
    val BLOCK_CHAIN_LINK_FENCE_BREAK = register("block.adorn.chain_link_fence.break")
    val BLOCK_CHAIN_LINK_FENCE_STEP = register("block.adorn.chain_link_fence.step")
    val BLOCK_CHAIN_LINK_FENCE_PLACE = register("block.adorn.chain_link_fence.place")
    val BLOCK_CHAIN_LINK_FENCE_HIT = register("block.adorn.chain_link_fence.hit")
    val BLOCK_CHAIN_LINK_FENCE_FALL = register("block.adorn.chain_link_fence.fall")

    val CHAIN_LINK_FENCE = BlockSoundGroup(
        1.0F,
        1.5F,
        BLOCK_CHAIN_LINK_FENCE_BREAK,
        BLOCK_CHAIN_LINK_FENCE_STEP,
        BLOCK_CHAIN_LINK_FENCE_PLACE,
        BLOCK_CHAIN_LINK_FENCE_HIT,
        BLOCK_CHAIN_LINK_FENCE_FALL
    )

    fun init() {}

    private fun register(name: String): SoundEvent =
        Registry.register(Registry.SOUND_EVENT, Adorn.id(name), SoundEvent(Adorn.id(name)))
}
