package juuxel.adorn.commonmixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import juuxel.adorn.block.SofaBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.attribute.BedRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
abstract class PlayerMixin extends LivingEntity {
    PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyReceiver(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/level/Level;)Z"))
    private BedRule modifyBedRuleForSofas(BedRule instance, Level world) {
        var pos = getSleepingPos().orElse(null);
        return pos != null && world.getBlockState(pos).getBlock() instanceof SofaBlock ? SofaBlock.modifyBedRuleForSofas(instance) : instance;
    }
}
