package juuxel.adorn.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public final class SitOnBlockCriterion extends AbstractCriterion<SitOnBlockCriterion.Conditions> {
    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, BlockPos pos) {
        trigger(player, conditions -> conditions.matches(player, pos));
    }

    public record Conditions(Optional<LootContextPredicate> player, BlockPredicate block) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player")
                .forGetter(Conditions::player),
            BlockPredicate.CODEC.fieldOf("block").forGetter(Conditions::block)
        ).apply(instance, Conditions::new));

        public boolean matches(ServerPlayerEntity player, BlockPos pos) {
            return block.test(player.getWorld(), pos);
        }
    }
}
