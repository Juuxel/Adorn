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

    @ModifyArg(method = "drawScrollableText(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;enableScissor(IIII)V"), index = 1)
    private static int modifyStartYForScissor(int y) {
        return modifyYForScissor(y);
    }

    @ModifyArg(method = "drawScrollableText(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;enableScissor(IIII)V"), index = 3)
    private static int modifyEndYForScissor(int y) {
        return modifyYForScissor(y);
    }

    @Unique
    private static int modifyYForScissor(int y) {
        var offset = ScrollEnvelope.OFFSET.get();
        return offset != null ? y - offset.intValue() : y;
    }
}
