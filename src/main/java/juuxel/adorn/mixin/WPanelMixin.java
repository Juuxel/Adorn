package juuxel.adorn.mixin;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.client.Scissors;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;

/**
 * A backported fix from LibGui for MC 1.15.
 */
@Mixin(WPanel.class)
public class WPanelMixin extends WWidget {
    @Redirect(method = "hit", at = @At(value = "INVOKE", target = "Lio/github/cottonmc/cotton/gui/widget/WWidget;hit(II)Lio/github/cottonmc/cotton/gui/widget/WWidget;"), remap = false)
    private WWidget onHit(WWidget child, int x, int y, int x2, int y2) {
        return child.hit(x2 - child.getX(), y2 - child.getY());
    }
}
