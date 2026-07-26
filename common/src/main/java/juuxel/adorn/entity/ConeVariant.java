package juuxel.adorn.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import juuxel.adorn.util.Dyes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ConeVariant(
    float weight,
    HolderSet<Fluid> floatsIn,
    boolean canBurn,
    Holder<SoundEvent> placeSound,
    Optional<ResourceKey<ConeVariant>> appearance
) {
    private static final Holder<SoundEvent> DEFAULT_PLACE_SOUND = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOOD_PLACE);
    private static final Holder<SoundEvent> STONE_PLACE_SOUND = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.STONE_PLACE);

    public static final Codec<ConeVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("weight", 1f).forGetter(ConeVariant::weight),
        RegistryCodecs.homogeneousList(Registries.FLUID).optionalFieldOf("floats_in", HolderSet.empty()).forGetter(ConeVariant::floatsIn),
        Codec.BOOL.optionalFieldOf("can_burn", true).forGetter(ConeVariant::canBurn),
        SoundEvent.CODEC.optionalFieldOf("place_sound", DEFAULT_PLACE_SOUND).forGetter(ConeVariant::placeSound),
        ResourceKey.codec(AdornRegistryKeys.CONE_VARIANT).optionalFieldOf("appearance").forGetter(ConeVariant::appearance)
    ).apply(instance, ConeVariant::new));

    public static final Codec<Holder<ConeVariant>> REGISTRY_CODEC = RegistryFixedCodec.create(AdornRegistryKeys.CONE_VARIANT);

    public static final StreamCodec<RegistryFriendlyByteBuf, ConeVariant> PACKET_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, ConeVariant::weight,
        ByteBufCodecs.holderSet(Registries.FLUID), ConeVariant::floatsIn,
        ByteBufCodecs.BOOL, ConeVariant::canBurn,
        SoundEvent.STREAM_CODEC, ConeVariant::placeSound,
        ByteBufCodecs.optional(ResourceKey.streamCodec(AdornRegistryKeys.CONE_VARIANT)), ConeVariant::appearance,
        ConeVariant::new
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ConeVariant>> ENTRY_PACKET_CODEC =
        ByteBufCodecs.holder(AdornRegistryKeys.CONE_VARIANT, PACKET_CODEC);

    public static final String DEFAULT_TRANSLATION_KEY = Util.makeDescriptionId("entity", AdornCommon.id("cone"));
    public static final Component DEFAULT_NAME = Component.translatable(DEFAULT_TRANSLATION_KEY);

    public static void bootstrap(BootstrapContext<ConeVariant> registerable) {
        var fluidRegistry = registerable.lookup(Registries.FLUID);
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

    private static ConeVariant createDefault(HolderGetter<Fluid> fluidRegistry) {
        return new ConeVariant(1f, fluidRegistry.getOrThrow(FluidTags.WATER), true, DEFAULT_PLACE_SOUND, Optional.empty());
    }

    public static Component getName(ResourceKey<ConeVariant> variant) {
        return Component.translatable(Util.makeDescriptionId("entity", variant.identifier().withSuffix("_cone")));
    }

    public static Component getName(Holder<ConeVariant> variant) {
        return variant.unwrapKey().map(ConeVariant::getName).orElse(DEFAULT_NAME);
    }

    public static final class Keys {
        private static final List<ResourceKey<ConeVariant>> ALL_BUILTIN = new ArrayList<>();
        private static final Map<ResourceKey<ConeVariant>, DyeColor> COLORS_BY_VARIANT = new IdentityHashMap<>();

        public static final ResourceKey<ConeVariant> WHITE = of("white", DyeColor.WHITE);
        public static final ResourceKey<ConeVariant> ORANGE = of("orange", DyeColor.ORANGE);
        public static final ResourceKey<ConeVariant> MAGENTA = of("magenta", DyeColor.MAGENTA);
        public static final ResourceKey<ConeVariant> LIGHT_BLUE = of("light_blue", DyeColor.LIGHT_BLUE);
        public static final ResourceKey<ConeVariant> YELLOW = of("yellow", DyeColor.YELLOW);
        public static final ResourceKey<ConeVariant> LIME = of("lime", DyeColor.LIME);
        public static final ResourceKey<ConeVariant> PINK = of("pink", DyeColor.PINK);
        public static final ResourceKey<ConeVariant> GRAY = of("gray", DyeColor.GRAY);
        public static final ResourceKey<ConeVariant> LIGHT_GRAY = of("light_gray", DyeColor.LIGHT_GRAY);
        public static final ResourceKey<ConeVariant> CYAN = of("cyan", DyeColor.CYAN);
        public static final ResourceKey<ConeVariant> PURPLE = of("purple", DyeColor.PURPLE);
        public static final ResourceKey<ConeVariant> BLUE = of("blue", DyeColor.BLUE);
        public static final ResourceKey<ConeVariant> BROWN = of("brown", DyeColor.BROWN);
        public static final ResourceKey<ConeVariant> GREEN = of("green", DyeColor.GREEN);
        public static final ResourceKey<ConeVariant> RED = of("red", DyeColor.RED);
        public static final ResourceKey<ConeVariant> BLACK = of("black", DyeColor.BLACK);
        public static final ResourceKey<ConeVariant> OBSIDIAN = of("obsidian");

        public static final Comparator<ResourceKey<ConeVariant>> COMPARATOR = (a, b) -> {
            @Nullable DyeColor colorA = COLORS_BY_VARIANT.get(a);
            @Nullable DyeColor colorB = COLORS_BY_VARIANT.get(b);

            if (colorA != null) {
                return colorB != null ? Dyes.DYES_IN_CREATIVE_INVENTORY_ORDER.indexOf(colorA) - Dyes.DYES_IN_CREATIVE_INVENTORY_ORDER.indexOf(colorB) : -1;
            } else if (colorB != null) {
                return 1;
            }

            return a.identifier().compareTo(b.identifier());
        };

        public static List<ResourceKey<ConeVariant>> getAllBuiltinVariants() {
            return Collections.unmodifiableList(ALL_BUILTIN);
        }

        private static ResourceKey<ConeVariant> of(String id) {
            var key = ResourceKey.create(AdornRegistryKeys.CONE_VARIANT, AdornCommon.id(id));
            ALL_BUILTIN.add(key);
            return key;
        }

        private static ResourceKey<ConeVariant> of(String id, DyeColor color) {
            var key = of(id);
            COLORS_BY_VARIANT.put(key, color);
            return key;
        }
    }
}
