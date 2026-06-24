package juuxel.adorn.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import juuxel.adorn.block.SofaBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.attribute.BedRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerEntity.class)
abstract class ServerPlayerEntityMixin {
    @ModifyReceiver(method = "trySleep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/BedRule;canSleep(Lnet/minecraft/world/World;)Z"))
    private BedRule modifyBedRuleForSofas(BedRule instance, World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof SofaBlock ? SofaBlock.modifyBedRuleForSofas(instance) : instance;
    }
}
