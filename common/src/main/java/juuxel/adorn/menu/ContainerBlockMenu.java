package juuxel.adorn.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerLevelAccess;

public interface ContainerBlockMenu {
    Container getInventory();
    ContainerLevelAccess getContext();
}
