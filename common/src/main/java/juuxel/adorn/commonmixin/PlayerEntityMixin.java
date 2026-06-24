package juuxel.adorn.commonmixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import juuxel.adorn.block.SofaBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.attribute.BedRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity {
    PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReceiver(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/World;)Z"))
    private BedRule modifyBedRuleForSofas(BedRule instance, World world) {
        var pos = getSleepingPosition().orElse(null);
        return pos != null && world.getBlockState(pos).getBlock() instanceof SofaBlock ? SofaBlock.modifyBedRuleForSofas(instance) : instance;
    }
}
