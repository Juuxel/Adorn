package juuxel.adorn.item;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.entity.AdornEntities;
import juuxel.adorn.entity.ConeVariant;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.gameevent.GameEvent;

public final class ConeItem extends Item {
    public ConeItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        var offsetPos = context.getClickedPos().relative(context.getClickedFace());

        if (context.getLevel() instanceof ServerLevel world) {
            AdornEntities.CONE.get().spawn(world, context.getItemInHand(), player, offsetPos, EntitySpawnReason.SPAWN_ITEM_USE, false, false);
            context.getLevel().gameEvent(player, GameEvent.ENTITY_PLACE, offsetPos);
        }

        var coneVariantComponent = context.getItemInHand().get(AdornComponentTypes.CONE_VARIANT.get());
        if (coneVariantComponent != null) {
            var variant = coneVariantComponent.variant()
                .unwrap(context.getLevel().registryAccess())
                .orElse(null);
            if (variant != null) {
                context.getLevel().playSound(player, offsetPos, variant.value().placeSound().value(), SoundSource.BLOCKS, 1f, 0.8f);
            }
        }

        context.getItemInHand().consume(1, player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Component getName(ItemStack stack) {
        var variantComponent = stack.get(AdornComponentTypes.CONE_VARIANT.get());
        if (variantComponent != null) {
            return ConeVariant.getName(variantComponent.variant());
        }

        return super.getName(stack);
    }
}
