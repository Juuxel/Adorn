package juuxel.adorn.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public record ConeVariant(
    float weight,
    RegistryEntryList<Fluid> floatsIn,
    boolean canBurn,
    RegistryEntry<SoundEvent> placeSound,
    Optional<RegistryKey<ConeVariant>> appearance
) {
    private static final RegistryEntry<SoundEvent> DEFAULT_PLACE_SOUND = Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_WOOD_PLACE);
    private static final RegistryEntry<SoundEvent> STONE_PLACE_SOUND = Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_STONE_PLACE);

    public static final Codec<ConeVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codecs.NON_NEGATIVE_FLOAT.optionalFieldOf("weight", 1f).forGetter(ConeVariant::weight),
        RegistryCodecs.entryList(RegistryKeys.FLUID).optionalFieldOf("floats_in", RegistryEntryList.empty()).forGetter(ConeVariant::floatsIn),
        Codec.BOOL.optionalFieldOf("can_burn", true).forGetter(ConeVariant::canBurn),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("place_sound", DEFAULT_PLACE_SOUND).forGetter(ConeVariant::placeSound),
        RegistryKey.createCodec(AdornRegistryKeys.CONE_VARIANT).optionalFieldOf("appearance").forGetter(ConeVariant::appearance)
    ).apply(instance, ConeVariant::new));

    public static final Codec<RegistryEntry<ConeVariant>> REGISTRY_CODEC = RegistryFixedCodec.of(AdornRegistryKeys.CONE_VARIANT);

    public static final PacketCodec<RegistryByteBuf, ConeVariant> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.FLOAT, ConeVariant::weight,
        PacketCodecs.registryEntryList(RegistryKeys.FLUID), ConeVariant::floatsIn,
        PacketCodecs.BOOLEAN, ConeVariant::canBurn,
        SoundEvent.ENTRY_PACKET_CODEC, ConeVariant::placeSound,
        PacketCodecs.optional(RegistryKey.createPacketCodec(AdornRegistryKeys.CONE_VARIANT)), ConeVariant::appearance,
        ConeVariant::new
    );

    public static final PacketCodec<RegistryByteBuf, RegistryEntry<ConeVariant>> ENTRY_PACKET_CODEC =
        PacketCodecs.registryEntry(AdornRegistryKeys.CONE_VARIANT, PACKET_CODEC);

    public static final String DEFAULT_TRANSLATION_KEY = Util.createTranslationKey("entity", AdornCommon.id("cone"));
    public static final Text DEFAULT_NAME = Text.translatable(DEFAULT_TRANSLATION_KEY);

    public static void bootstrap(Registerable<ConeVariant> registerable) {
        var fluidRegistry = registerable.getRegistryLookup(RegistryKeys.FLUID);
        registerable.register(Keys.WHITE, createDefault(fluidRegistry));
        registerable.register(Keys.ORANGE, createDefault(fluidRegistry));
        registerable.register(Keys.MAGENTA, createDefault(fluidRegistry));
        registerable.register(Keys.LIGHT_BLUE, createDefault(fluidRegistry));
        registerable.register(Keys.YELLOW, createDefault(fluidRegistry));
        registerable.register(Keys.LIME, createDefault(fluidRegistry));
        registerable.register(Keys.PINK, createDefault(fluidRegistry));
        registerable.register(Keys.GRAY, createDefault(fluidRegistry));
        registerable.register(Keys.LIGHT_GRAY, createDefault(fluidRegistry));
        registerable.register(Keys.CYAN, createDefault(fluidRegistry));
        registerable.register(Keys.PURPLE, createDefault(fluidRegistry));
        registerable.register(Keys.BLUE, createDefault(fluidRegistry));
        registerable.register(Keys.BROWN, createDefault(fluidRegistry));
        registerable.register(Keys.GREEN, createDefault(fluidRegistry));
        registerable.register(Keys.RED, createDefault(fluidRegistry));
        registerable.register(Keys.BLACK, createDefault(fluidRegistry));
        registerable.register(Keys.OBSIDIAN, new ConeVariant(2f, fluidRegistry.getOrThrow(FluidTags.LAVA), false, STONE_PLACE_SOUND, Optional.empty()));
    }

    private static ConeVariant createDefault(RegistryEntryLookup<Fluid> fluidRegistry) {
        return new ConeVariant(1f, fluidRegistry.getOrThrow(FluidTags.WATER), true, DEFAULT_PLACE_SOUND, Optional.empty());
    }

    public static Text getName(RegistryKey<ConeVariant> variant) {
        return Text.translatable(Util.createTranslationKey("entity", variant.getValue().withSuffixedPath("_cone")));
    }

    public static Text getName(RegistryEntry<ConeVariant> variant) {
        return variant.getKey().map(ConeVariant::getName).orElse(DEFAULT_NAME);
    }

    public static Text getName(LazyRegistryEntryReference<ConeVariant> variant) {
        return variant.getKey().map(ConeVariant::getName).orElse(DEFAULT_NAME);
    }

    public static final class Keys {
        private static final List<RegistryKey<ConeVariant>> ALL_BUILTIN = new ArrayList<>();

        public static final RegistryKey<ConeVariant> WHITE = of("white");
        public static final RegistryKey<ConeVariant> ORANGE = of("orange");
        public static final RegistryKey<ConeVariant> MAGENTA = of("magenta");
        public static final RegistryKey<ConeVariant> LIGHT_BLUE = of("light_blue");
        public static final RegistryKey<ConeVariant> YELLOW = of("yellow");
        public static final RegistryKey<ConeVariant> LIME = of("lime");
        public static final RegistryKey<ConeVariant> PINK = of("pink");
        public static final RegistryKey<ConeVariant> GRAY = of("gray");
        public static final RegistryKey<ConeVariant> LIGHT_GRAY = of("light_gray");
        public static final RegistryKey<ConeVariant> CYAN = of("cyan");
        public static final RegistryKey<ConeVariant> PURPLE = of("purple");
        public static final RegistryKey<ConeVariant> BLUE = of("blue");
        public static final RegistryKey<ConeVariant> BROWN = of("brown");
        public static final RegistryKey<ConeVariant> GREEN = of("green");
        public static final RegistryKey<ConeVariant> RED = of("red");
        public static final RegistryKey<ConeVariant> BLACK = of("black");
        public static final RegistryKey<ConeVariant> OBSIDIAN = of("obsidian");

        public static List<RegistryKey<ConeVariant>> getAllBuiltinVariants() {
            return Collections.unmodifiableList(ALL_BUILTIN);
        }

        private static RegistryKey<ConeVariant> of(String id) {
            var key = RegistryKey.of(AdornRegistryKeys.CONE_VARIANT, AdornCommon.id(id));
            ALL_BUILTIN.add(key);
            return key;
        }
    }
}
