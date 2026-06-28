package juuxel.adorn.commonmixin.client;

import juuxel.adorn.client.gui.widget.ScrollEnvelope;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClickableWidget.class)
abstract class ClickableWidgetMixin {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;scissorContains(II)Z"), index = 1)
    private int modifyYForHoverScissor(int y) {
        return modifyYForScissor(y);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/TooltipState;render(Lnet/minecraft/client/gui/DrawContext;IIZZLnet/minecraft/client/gui/ScreenRect;)V"), index = 2)
    private int modifyYForTooltip(int y) {
        return modifyYForScissor(y);
    }

    @Unique
    private static int modifyYForScissor(int y) {
        var offset = ScrollEnvelope.OFFSET.get();
        return offset != null ? y - offset.intValue() : y;
    }
}
