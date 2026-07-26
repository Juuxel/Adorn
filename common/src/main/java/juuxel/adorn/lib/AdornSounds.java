package juuxel.adorn.lib;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.lib.registry.Registrar;
import juuxel.adorn.lib.registry.RegistrarFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public final class AdornSounds {
    public static final Registrar<SoundEvent> SOUNDS = RegistrarFactory.get().create(Registries.SOUND_EVENT);

    public static final Registered<SoundEvent> BLOCK_CHAIN_LINK_FENCE_BREAK = register("block.adorn.chain_link_fence.break");
    public static final Registered<SoundEvent> BLOCK_CHAIN_LINK_FENCE_STEP = register("block.adorn.chain_link_fence.step");
    public static final Registered<SoundEvent> BLOCK_CHAIN_LINK_FENCE_PLACE = register("block.adorn.chain_link_fence.place");
    public static final Registered<SoundEvent> BLOCK_CHAIN_LINK_FENCE_HIT = register("block.adorn.chain_link_fence.hit");
    public static final Registered<SoundEvent> BLOCK_CHAIN_LINK_FENCE_FALL = register("block.adorn.chain_link_fence.fall");
    public static final Registered<SoundEvent> ITEM_WATERING_CAN_WATER = register("item.adorn.watering_can.water");

    public static final SoundType CHAIN_LINK_FENCE = new LazyBlockSoundGroup(
        1.0F,
        1.5F,
        BLOCK_CHAIN_LINK_FENCE_BREAK,
        BLOCK_CHAIN_LINK_FENCE_STEP,
        BLOCK_CHAIN_LINK_FENCE_PLACE,
        BLOCK_CHAIN_LINK_FENCE_HIT,
        BLOCK_CHAIN_LINK_FENCE_FALL
    );

    public static void init() {
    }

    private static Registered<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(AdornCommon.id(name)));
    }

    private static final class LazyBlockSoundGroup extends SoundType {
        private final Supplier<SoundEvent> breakSound;
        private final Supplier<SoundEvent> stepSound;
        private final Supplier<SoundEvent> placeSound;
        private final Supplier<SoundEvent> hitSound;
        private final Supplier<SoundEvent> fallSound;

        LazyBlockSoundGroup(
            float volume,
            float pitch,
            Supplier<SoundEvent> breakSound,
            Supplier<SoundEvent> stepSound,
            Supplier<SoundEvent> placeSound,
            Supplier<SoundEvent> hitSound,
            Supplier<SoundEvent> fallSound
        ) {
            super(volume, pitch, null, null, null, null, null);
            this.breakSound = breakSound;
            this.stepSound = stepSound;
            this.placeSound = placeSound;
            this.hitSound = hitSound;
            this.fallSound = fallSound;
        }

        @Override
        public SoundEvent getBreakSound() {
            return breakSound.get();
        }

        @Override
        public SoundEvent getStepSound() {
            return stepSound.get();
        }

        @Override
        public SoundEvent getPlaceSound() {
            return placeSound.get();
        }

        @Override
        public SoundEvent getHitSound() {
            return hitSound.get();
        }

        @Override
        public SoundEvent getFallSound() {
            return fallSound.get();
        }
    }
}
