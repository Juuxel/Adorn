package juuxel.adorn.lib

import juuxel.adorn.Adorn
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.registry.Registry

object AdornSounds {
    val BLOCK_CHAIN_LINK_FENCE_STEP = register("block.adorn.chain_link_fence.step")

    val CHAIN_LINK_FENCE = BlockSoundGroup(
        1.0F,
        1.5F,
        SoundEvents.BLOCK_METAL_BREAK,
        BLOCK_CHAIN_LINK_FENCE_STEP,
        SoundEvents.BLOCK_METAL_PLACE,
        SoundEvents.BLOCK_METAL_HIT,
        SoundEvents.BLOCK_METAL_FALL
    )

    fun init() {}

    private fun register(name: String): SoundEvent =
        Registry.register(Registry.SOUND_EVENT, Adorn.id(name), SoundEvent(Adorn.id(name)))
}
