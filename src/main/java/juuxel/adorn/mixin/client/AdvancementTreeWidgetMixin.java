package juuxel.adorn.mixin.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.advancement.AdvancementTreeWidget;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// lol, fixing thonkjang bugs
// TODO: Remove this or move it somewhere else.
// The reason I have this mixin at all is that it makes my advancement tab look good :^)
@Mixin(AdvancementTreeWidget.class)
public class AdvancementTreeWidgetMixin {
    @Inject(method = "drawIcon", at = @At("HEAD"))
    private void onDrawIconHead(int int_1, int int_2, ItemRenderer itemRenderer_1, CallbackInfo info) {
        GlStateManager.enableDepthTest();
    }

    @Inject(method = "drawIcon", at = @At("RETURN"))
    private void onDrawIconReturn(int int_1, int int_2, ItemRenderer itemRenderer_1, CallbackInfo info) {
        GlStateManager.disableDepthTest();
    }
}
