package juuxel.adorn.mixin.client;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// TODO: https://github.com/CottonMC/LibGui/pull/3
@Mixin(WWidget.class)
public abstract class WWidgetMixin {
    @Shadow
    protected abstract void renderTooltip(int tX, int tY);

    @Redirect(method = "paintForeground", at = @At(value = "INVOKE", target = "Lio/github/cottonmc/cotton/gui/widget/WWidget;renderTooltip(II)V", remap = false), remap = false)
    private void onRenderTooltip(WWidget wWidget, int tX, int tY, int x, int y, int mouseX, int mouseY) {
        renderTooltip(mouseX, mouseY);
    }
}
