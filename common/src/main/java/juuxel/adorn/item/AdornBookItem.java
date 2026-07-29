package juuxel.adorn.item;

import juuxel.adorn.networking.OpenBookS2CMessage;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public final class AdornBookItem extends Item {
    public static BookTooltipProvider tooltipProvider = (_, _) -> {};

    private final Identifier bookId;

    public AdornBookItem(Identifier bookId, Properties settings) {
        super(settings);
        this.bookId = bookId;
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide()) {
            PlatformBridges.get().getNetwork().sendToClient(user, new OpenBookS2CMessage(bookId));
        }
        user.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
        tooltipProvider.appendTooltip(bookId, textConsumer);
    }

    @FunctionalInterface
    public interface BookTooltipProvider {
        void appendTooltip(Identifier bookId, Consumer<Component> builder);
    }
}
