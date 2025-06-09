package juuxel.adorn.client.renderer;

import juuxel.adorn.entity.ConeVariant;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.registry.RegistryKey;

public final class ConeEntityRenderState extends EntityRenderState {
    public RegistryKey<ConeVariant> variant;
}
