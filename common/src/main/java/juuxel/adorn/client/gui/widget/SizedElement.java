package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.components.events.GuiEventListener;

public interface SizedElement extends GuiEventListener {
    int getWidth();
    int getHeight();
}
