package juuxel.adorn.entity;

import com.google.common.collect.Maps;
import juuxel.adorn.AdornCommon;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

public enum ConeVariant {
    WHITE("white"),
    ORANGE("orange"),
    MAGENTA("magenta"),
    LIGHT_BLUE("light_blue"),
    YELLOW("yellow"),
    LIME("lime"),
    PINK("pink"),
    GRAY("gray"),
    LIGHT_GRAY("light_gray"),
    CYAN("cyan"),
    PURPLE("purple"),
    BLUE("blue"),
    BROWN("brown"),
    GREEN("green"),
    RED("red"),
    BLACK("black"),
    OBSIDIAN("obsidian"),
    ;

    private static final ConeVariant[] VALUES = values();
    private static final Map<String, ConeVariant> BY_ID = Maps.uniqueIndex(Arrays.asList(VALUES), ConeVariant::id);
    private static final RegistryEntry<SoundEvent> DEFAULT_PLACE_SOUND = Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_WOOD_PLACE);
    private static final RegistryEntry<SoundEvent> STONE_PLACE_SOUND = Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_STONE_PLACE);

    public static final String DEFAULT_TRANSLATION_KEY = Util.createTranslationKey("entity", AdornCommon.id("cone"));

    private final String id;
    private final Text displayName;

    ConeVariant(String id) {
        this.id = id;
        this.displayName = Text.translatable(Util.createTranslationKey("entity", AdornCommon.id(id + "_cone")));
    }

    public static ConeVariant fromOrdinal(int ordinal) {
        return VALUES[ordinal];
    }

    public static @Nullable ConeVariant fromId(String id) {
        return BY_ID.get(id);
    }

    public String id() {
        return id;
    }

    public float weight() {
        return this == OBSIDIAN ? 2 : 1;
    }

    public boolean floatsIn(FluidState state) {
        return this == OBSIDIAN ? state.isIn(FluidTags.LAVA) : state.isIn(FluidTags.WATER);
    }

    public boolean canBurn() {
        return this != OBSIDIAN;
    }

    public RegistryEntry<SoundEvent> placeSound() {
        return this == OBSIDIAN ? STONE_PLACE_SOUND : DEFAULT_PLACE_SOUND;
    }

    public Text displayName() {
        return displayName;
    }
}
