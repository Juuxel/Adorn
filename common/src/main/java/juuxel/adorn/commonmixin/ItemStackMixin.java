package juuxel.adorn.commonmixin;

import juuxel.adorn.component.AdornComponentTypes;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
    @Shadow
    public abstract <T extends TooltipAppender> void appendComponentTooltip(ComponentType<T> componentType, Item.TooltipContext context, TooltipDisplayComponent displayComponent, @Nullable PlayerEntity playerEntity, Consumer<Text> consumer, TooltipType tooltipType);

    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendComponentTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/component/type/TooltipDisplayComponent;Lnet/minecraft/entity/player/PlayerEntity;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V", ordinal = 0))
    private void appendTooltipsOfAdornComponents(Item.TooltipContext context, TooltipDisplayComponent displayComponent, PlayerEntity player, TooltipType type, Consumer<Text> textConsumer, CallbackInfo info) {
        appendComponentTooltip(AdornComponentTypes.TRADE_OWNER.get(), context, displayComponent, player, textConsumer, type);
        appendComponentTooltip(AdornComponentTypes.FERTILIZER_LEVEL.get(), context, displayComponent, player, textConsumer, type);
        appendComponentTooltip(AdornComponentTypes.BOOK.get(), context, displayComponent, player, textConsumer, type);
    }
}
