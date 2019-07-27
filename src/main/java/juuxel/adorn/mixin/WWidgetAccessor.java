package juuxel.adorn.mixin;

import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WWidget.class)
public interface WWidgetAccessor {
    @Accessor
    void setParent(WPanel parent);
}
