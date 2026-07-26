package juuxel.adorn.entity;

import juuxel.adorn.util.InlineServices;
import juuxel.adorn.util.Services;
import net.minecraft.world.entity.Entity;

@InlineServices
public interface EntityBridge {
    boolean isInFluid(Entity entity);

    @InlineServices.Getter
    static EntityBridge get() {
        return Services.load(EntityBridge.class);
    }
}
