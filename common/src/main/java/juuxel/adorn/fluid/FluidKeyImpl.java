package juuxel.adorn.fluid;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import juuxel.adorn.util.EntryOrTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class FluidKeyImpl {
    private static final Codec<Simple> SIMPLE_CODEC = EntryOrTag.codec(Registries.FLUID).xmap(Simple::new, Simple::fluids);

    public static final Codec<FluidKey> CODEC = Codec.either(
        SIMPLE_CODEC,
        SIMPLE_CODEC.listOf().xmap(OfArray::new, OfArray::children)
    ).xmap(
        Either::unwrap,
        key -> switch (key) {
            case Simple simple -> Either.left(simple);
            case OfArray ofArray -> Either.right(ofArray);
        }
    );

    record Simple(EntryOrTag<Fluid> fluids) implements FluidKey {
        @Override
        public Set<Fluid> getFluids() {
            return switch (fluids) {
                case EntryOrTag.OfEntry(var fluid) -> Set.of(fluid);
                case EntryOrTag.OfTag(var tag) -> BuiltInRegistries.FLUID.get(tag)
                    .stream()
                    .flatMap(HolderSet::stream)
                    .map(Holder::value)
                    .collect(Collectors.toSet());
            };
        }

        @Override
        public boolean matches(Fluid fluid) {
            return switch (fluids) {
                case EntryOrTag.OfEntry(var keyFluid) -> fluid.isSame(keyFluid);
                case EntryOrTag.OfTag(var tag) -> fluid.is(tag);
            };
        }
    }

    record OfArray(List<Simple> children) implements FluidKey {
        @Override
        public Set<Fluid> getFluids() {
            return children.stream()
                .flatMap(child -> child.getFluids().stream())
                .collect(Collectors.toSet());
        }

        @Override
        public boolean matches(Fluid fluid) {
            for (var child : children) {
                if (child.matches(fluid)) return true;
            }

            return false;
        }
    }
}
