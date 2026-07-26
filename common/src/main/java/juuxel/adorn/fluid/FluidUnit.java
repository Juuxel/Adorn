package juuxel.adorn.fluid;

import com.mojang.serialization.Codec;
import juuxel.adorn.util.Displayable;
import juuxel.adorn.util.MixedFraction;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Fluid volume units. This class is used for doing fluid volume
 * math in the common module (fluid-based recipes).
 *
 * <p>The platform-specific unit is available via
 * {@link juuxel.adorn.platform.FluidBridge#getFluidUnit}.
 */
public enum FluidUnit implements Displayable {
    /** Litres. Defined as one thousandth of a cubic metre ({@link #getBucketVolume()} = 1000). */
    LITRE("litres", 1000),

    /** Droplets. Defined as 1/81 000 of a cubic metre ({@link #getBucketVolume()} = 81 000). */
    DROPLET("droplets", 81_000);

    public static final Codec<FluidUnit> CODEC = Codec.STRING.xmap(FluidUnit::byId, FluidUnit::getId);
    private static final Map<String, FluidUnit> BY_ID = Arrays.stream(values())
        .collect(Collectors.toMap(FluidUnit::getId, Function.identity()));

    private final String id;
    private final long bucketVolume;
    private final Component displayName;
    private final Component symbol;

    FluidUnit(String id, long bucketVolume) {
        this.id = id;
        this.bucketVolume = bucketVolume;
        this.displayName = Component.translatable("gui.adorn.fluid_unit.%s.name".formatted(id));
        this.symbol = Component.translatable("gui.adorn.fluid_unit.%s.symbol".formatted(id));
    }

    public String getId() {
        return id;
    }

    public long getBucketVolume() {
        return bucketVolume;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }

    public Component getSymbol() {
        return symbol;
    }

    /**
     * Returns the fluid unit with the specified {@linkplain #getId() ID},
     * or null if not found.
     */
    public static @Nullable FluidUnit byId(String id) {
        return BY_ID.get(id.toLowerCase(Locale.ROOT));
    }

    /**
     * Converts a volume between two fluid units. Potentially lossy, use with caution!
     */
    public static long convert(long volume, FluidUnit from, FluidUnit to) {
        if (from == to) return volume;
        return volume * to.getBucketVolume() / from.getBucketVolume();
    }

    /**
     * Converts a volume between two fluid units losslessly, returning a mixed fraction.
     */
    public static MixedFraction losslessConvert(long volume, FluidUnit from, FluidUnit to) {
        if (from == to) return MixedFraction.whole(volume);
        return MixedFraction.of(volume * to.getBucketVolume(), from.getBucketVolume());
    }

    /**
     * Converts a volume between two fluid units.
     * This variant is meant to be used for rendering.
     */
    public static double convertAsDouble(double volume, FluidUnit from, FluidUnit to) {
        if (from == to) return volume;
        return volume * (double) to.getBucketVolume() / (double) from.getBucketVolume();
    }

    /**
     * Compares two fluid volumes with specified units.
     */
    public static int compareVolumes(long volume1, FluidUnit unit1, long volume2, FluidUnit unit2) {
        if (unit1 == unit2) {
            return Long.compare(volume1, volume2);
        } else if (unit1.getBucketVolume() > unit2.getBucketVolume()) {
            return Long.compare(volume1, volume2 * unit1.getBucketVolume() / unit2.getBucketVolume());
        } else {
            return Long.compare(volume1 * unit2.getBucketVolume() / unit1.getBucketVolume(), volume2);
        }
    }

    /**
     * Compares the amounts of two fluid references.
     */
    public static int compareVolumes(HasFluidAmount volume1, HasFluidAmount volume2) {
        return compareVolumes(volume1.getAmount(), volume1.getUnit(), volume2.getAmount(), volume2.getUnit());
    }
}
