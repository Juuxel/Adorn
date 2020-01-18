package juuxel.adorn.mixin;

import juuxel.adorn.lib.AdornTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyVariable(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Vec3d;z:D", ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;")))
    private Block changeClimbablesIntoLadders(Block original) {
        return original.matches(AdornTags.CLIMBABLE) ? Blocks.LADDER : original;
    }
}
