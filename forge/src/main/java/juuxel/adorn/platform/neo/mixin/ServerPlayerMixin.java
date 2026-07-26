package juuxel.adorn.platform.neo.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import juuxel.adorn.block.SofaBlock;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.attribute.BedRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
abstract class ServerPlayerMixin {
    @ModifyReceiver(method = "lambda$startSleepInBed$15", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/level/Level;)Z"))
    private BedRule modifyBedRuleForSofas(BedRule instance, Level world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof SofaBlock ? SofaBlock.modifyBedRuleForSofas(instance) : instance;
    }
}
