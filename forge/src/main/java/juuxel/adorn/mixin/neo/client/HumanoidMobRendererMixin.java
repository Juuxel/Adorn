package juuxel.adorn.mixin.neo.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import juuxel.adorn.item.AdornItems;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidMobRenderer.class)
abstract class HumanoidMobRendererMixin {
    @WrapOperation(method = "getEquipmentIfRenderable", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;shouldRender(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z"))
    private static boolean storeConesInRenderState(ItemStack stack, EquipmentSlot slot, Operation<Boolean> original) {
        return original.call(stack, slot) || (slot == EquipmentSlot.HEAD && stack.is(AdornItems.CONE.get()));
    }
}
