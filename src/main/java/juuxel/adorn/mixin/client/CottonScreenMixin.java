package juuxel.adorn.mixin.client;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CottonScreen.class)
public abstract class CottonScreenMixin<T extends CottonScreenController> extends ContainerScreen<T> {
    public CottonScreenMixin(T container_1, PlayerInventory playerInventory_1, Component component_1) {
        super(container_1, playerInventory_1, component_1);
    }

    @Inject(method = "reposition",
            at = @At(value = "INVOKE",
                    target = "Lio/github/cottonmc/cotton/gui/widget/WPanel;validate(Lio/github/cottonmc/cotton/gui/CottonScreenController;)V",
                    remap = false,
                    shift = At.Shift.BEFORE
            ),
            remap = false
    )
    private void onReposition(CallbackInfo info) {
        // See CottonMC/cotton#21
        container.slotList.clear();
    }
}
