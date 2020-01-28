package juuxel.adorn.mixin.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor {
    // why is this protected, thonkjang?
    @Invoker
    void callRenderComponentHoverEffect(Text component, int x, int y);
}
