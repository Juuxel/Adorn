package juuxel.adorn.commonmixin.client;

import juuxel.adorn.client.gui.widget.ScrollEnvelope;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractWidget.class)
abstract class AbstractWidgetMixin {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;containsPointInScissor(II)Z"), index = 1)
    private int modifyYForHoverScissor(int y) {
        return modifyYForScissor(y);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/WidgetTooltipHolder;refreshTooltipForNextRenderPass(Lnet/minecraft/client/gui/GuiGraphics;IIZZLnet/minecraft/client/gui/navigation/ScreenRectangle;)V"), index = 2)
    private int modifyYForTooltip(int y) {
        return modifyYForScissor(y);
    }

    @Unique
    private static int modifyYForScissor(int y) {
        var offset = ScrollEnvelope.OFFSET.get();
        return offset != null ? y - offset.intValue() : y;
    }
}
