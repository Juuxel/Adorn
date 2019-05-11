package juuxel.adorn.mixin.client;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.minecraft.client.gui.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Lazy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CottonScreen.class)
public abstract class CottonScreenMixin<T extends CottonScreenController> extends ContainerScreen<T> {
    @Unique
    private final Lazy<Boolean> WITHOUT_PR_20 = new Lazy<>(() -> {
        Component superTitle = super.getTitle();
        return superTitle instanceof TranslatableComponent && superTitle.getText().equals("");
    });

    public CottonScreenMixin(T container_1, PlayerInventory playerInventory_1, Component component_1) {
        super(container_1, playerInventory_1, component_1);
    }

    @Inject(method = "drawBackground", at = @At("RETURN"))
    private void onDrawBackground(float partialTicks, int mouseX, int mouseY, CallbackInfo info) {
        if (WITHOUT_PR_20.get() && getTitle() != null) {
            font.draw(getTitle().getFormattedText(), left, top, container.getTitleColor());
        }
    }
}
