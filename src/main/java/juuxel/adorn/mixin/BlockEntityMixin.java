package juuxel.adorn.mixin;

import juuxel.adorn.upgrade.BlockEntityUpdates;
import net.minecraft.block.entity.BlockEntity;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockEntity.class)
abstract class BlockEntityMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @ModifyVariable(method = "createFromTag", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/NbtCompound;getString(Ljava/lang/String;)Ljava/lang/String;"))
    private static String upgradeAdornBlockEntities(String id) {
        if (id.startsWith("adorn:")) {
            String newId = BlockEntityUpdates.get(id);
            if (!id.equals(newId)) {
                LOGGER.info("[Adorn] Upgraded {} to {}", id, newId);
                return newId;
            }
        }
        return id;
    }
}
