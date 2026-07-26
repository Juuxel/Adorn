package juuxel.adorn.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

import java.util.Optional;

public final class SitOnBlockCriterion extends SimpleCriterionTrigger<SitOnBlockCriterion.Conditions> {
    @Override
    public Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayer player, BlockPos pos) {
        trigger(player, conditions -> conditions.matches(player, pos));
    }

    public record Conditions(Optional<ContextAwarePredicate> player, BlockPredicate block) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                .forGetter(Conditions::player),
            BlockPredicate.CODEC.fieldOf("block").forGetter(Conditions::block)
        ).apply(instance, Conditions::new));

        public boolean matches(ServerPlayer player, BlockPos pos) {
            return block.matches(player.level(), pos);
        }
    }
}
