package juuxel.adorn.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import juuxel.adorn.block.AdornBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
    @WrapOperation(
        method = "canEnterTrapdoor",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"),
        allow = 1
    )
    private boolean allowStoneLadders(BlockState state, Block block, Operation<Boolean> original) {
        return original.call(state, block) || state.isOf(AdornBlocks.STONE_LADDER.get());
    }
}
