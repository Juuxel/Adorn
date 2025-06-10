package juuxel.adorn.item;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.ConeVariant;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;

public final class ConeItem extends Item {
    public ConeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var player = context.getPlayer();
        var offsetPos = context.getBlockPos().offset(context.getSide());

        if (context.getWorld() instanceof ServerWorld world) {
            AdornEntities.CONE.get().spawnFromItemStack(world, context.getStack(), player, offsetPos, SpawnReason.SPAWN_ITEM_USE, false, false);
            context.getWorld().emitGameEvent(player, GameEvent.ENTITY_PLACE, offsetPos);
        }

        var coneVariantComponent = context.getStack().get(AdornComponentTypes.CONE_VARIANT.get());
        if (coneVariantComponent != null) {
            var variant = coneVariantComponent.variant()
                .resolveEntry(context.getWorld().getRegistryManager())
                .orElse(null);
            if (variant != null) {
                context.getWorld().playSound(player, offsetPos, variant.value().placeSound().value(), SoundCategory.BLOCKS, 1f, 0.8f);
            }
        }

        context.getStack().decrementUnlessCreative(1, player);
        return ActionResult.SUCCESS;
    }

    @Override
    public Text getName(ItemStack stack) {
        var variantComponent = stack.get(AdornComponentTypes.CONE_VARIANT.get());
        if (variantComponent != null) {
            return ConeVariant.getName(variantComponent.variant());
        }

        return super.getName(stack);
    }
}
