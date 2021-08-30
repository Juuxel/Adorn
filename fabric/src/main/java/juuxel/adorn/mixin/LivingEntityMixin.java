package juuxel.adorn.mixin;

import juuxel.adorn.block.SofaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    // Lambda: Optional.ifPresent in wakeUp()
    @Redirect(method = "method_18404", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$AbstractBlockState;getBlock()Lnet/minecraft/block/Block;", ordinal = 0))
    private Block swapBedInPlaceOfSofa(BlockState state) {
        return state.getBlock() instanceof SofaBlock ? Blocks.RED_BED : state.getBlock();
    }
}
