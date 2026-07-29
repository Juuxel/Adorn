package juuxel.adorn.client.commonmixin;

import juuxel.adorn.client.gui.widget.ScrollEnvelope;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractWidget.class)
abstract class AbstractWidgetMixin {
    @ModifyArg(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;containsPointInScissor(II)Z"), index = 1)
    private int modifyYForHoverScissor(int y) {
        return ScrollEnvelope.getRealMouseY(y);
    }

    @ModifyArg(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/WidgetTooltipHolder;refreshTooltipForNextRenderPass(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIZZLnet/minecraft/client/gui/navigation/ScreenRectangle;)V"), index = 2)
    private int modifyYForTooltip(int y) {
        return ScrollEnvelope.getRealMouseY(y);
    }
}
