package juuxel.adorn.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.lib.registry.AdornRegistryKeys;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;

public record ConeVariant(float weight, boolean canFloat, boolean canBurn) {
    public static final Codec<ConeVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.optionalFieldOf("weight", 1f).forGetter(ConeVariant::weight),
        Codec.BOOL.optionalFieldOf("can_float", true).forGetter(ConeVariant::canFloat),
        Codec.BOOL.optionalFieldOf("can_burn", true).forGetter(ConeVariant::canBurn)
    ).apply(instance, ConeVariant::new));

    public static final Codec<RegistryEntry<ConeVariant>> REGISTRY_CODEC = RegistryElementCodec.of(AdornRegistryKeys.CONE_VARIANT, CODEC);

    public static final PacketCodec<PacketByteBuf, ConeVariant> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.FLOAT, ConeVariant::weight,
        PacketCodecs.BOOLEAN, ConeVariant::canFloat,
        PacketCodecs.BOOLEAN, ConeVariant::canBurn,
        ConeVariant::new
    );

    public static final PacketCodec<RegistryByteBuf, RegistryEntry<ConeVariant>> ENTRY_PACKET_CODEC =
        PacketCodecs.registryEntry(AdornRegistryKeys.CONE_VARIANT, PACKET_CODEC);

    public static void bootstrap(Registerable<ConeVariant> registerable) {
        registerable.register(Keys.WHITE, createDefault());
        registerable.register(Keys.ORANGE, createDefault());
        registerable.register(Keys.MAGENTA, createDefault());
        registerable.register(Keys.LIGHT_BLUE, createDefault());
        registerable.register(Keys.YELLOW, createDefault());
        registerable.register(Keys.LIME, createDefault());
        registerable.register(Keys.PINK, createDefault());
        registerable.register(Keys.GRAY, createDefault());
        registerable.register(Keys.LIGHT_GRAY, createDefault());
        registerable.register(Keys.CYAN, createDefault());
        registerable.register(Keys.PURPLE, createDefault());
        registerable.register(Keys.BLUE, createDefault());
        registerable.register(Keys.BROWN, createDefault());
        registerable.register(Keys.GREEN, createDefault());
        registerable.register(Keys.RED, createDefault());
        registerable.register(Keys.BLACK, createDefault());
    }

    private static ConeVariant createDefault() {
        return new ConeVariant(1f, true, true);
    }

    public static final class Keys {
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

        private static RegistryKey<ConeVariant> of(String id) {
            return RegistryKey.of(AdornRegistryKeys.CONE_VARIANT, AdornCommon.id(id));
        }
    }
}
