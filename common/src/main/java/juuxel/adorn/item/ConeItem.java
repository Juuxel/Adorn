package juuxel.adorn.item;

import juuxel.adorn.entity.AdornEntities;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;

public final class ConeItem extends Item {
    private final DyeColor color;

    public ConeItem(DyeColor color, Settings settings) {
        super(settings);
        this.color = color;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld() instanceof ServerWorld world) {
            var offsetPos = context.getBlockPos().offset(context.getSide());
            var cone = AdornEntities.CONE.get().spawn(world, offsetPos, SpawnReason.SPAWN_ITEM_USE);
            cone.setColor(color);
        }

        context.getStack().decrementUnlessCreative(1, context.getPlayer());
        return ActionResult.SUCCESS;
    }
}
