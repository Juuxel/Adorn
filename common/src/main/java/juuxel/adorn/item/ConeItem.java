package juuxel.adorn.item;

import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.ConeEntity;
import juuxel.adorn.entity.ConeVariant;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;

import java.util.function.Consumer;

public class ConeItem extends ItemWithDescription {
    private static final String DESCRIPTION_KEY = "entity.adorn.cone.description";
    private final ConeVariant variant;

    public ConeItem(ConeVariant variant, Settings settings) {
        super(settings);
        this.variant = variant;
    }

    public ConeVariant getVariant() {
        return variant;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var player = context.getPlayer();
        var offsetPos = context.getBlockPos().offset(context.getSide());

        if (context.getWorld() instanceof ServerWorld world) {
            Consumer<ConeEntity> callback = EntityType.copier(entity -> entity.setVariant(variant), world, context.getStack(), player);
            AdornEntities.CONE.get().spawn(world, callback, offsetPos, SpawnReason.SPAWN_EGG, false, false);
            context.getWorld().emitGameEvent(player, GameEvent.ENTITY_PLACE, offsetPos);
        }

        context.getWorld().playSound(player, offsetPos, variant.placeSound().value(), SoundCategory.BLOCKS, 1f, 0.8f);
        context.getStack().decrementUnlessCreative(1, player);
        return ActionResult.SUCCESS;
    }

    @Override
    public Text getName(ItemStack stack) {
        return variant.displayName();
    }

    @Override
    protected String getDescriptionKey(ItemStack stack) {
        return DESCRIPTION_KEY;
    }
}
