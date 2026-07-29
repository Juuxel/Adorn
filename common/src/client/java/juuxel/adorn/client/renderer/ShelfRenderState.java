package juuxel.adorn.client.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public final class ShelfRenderState extends BlockEntityRenderState {
    public ItemStackRenderState firstItem;
    public ItemStackRenderState secondItem;
}
