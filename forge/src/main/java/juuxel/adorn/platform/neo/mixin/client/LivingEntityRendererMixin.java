package juuxel.adorn.platform.neo.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import juuxel.adorn.item.AdornItems;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin {
    @WrapOperation(method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;hasModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;)Z"))
    private boolean disableConeDefaultItemRendering(ItemStack stack, EquipmentSlot slot, Operation<Boolean> original) {
        return original.call(stack, slot) || (slot == EquipmentSlot.HEAD && stack.isOf(AdornItems.CONE.get()));
    }
}
