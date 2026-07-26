package juuxel.adorn.item;

import juuxel.adorn.networking.OpenBookS2CMessage;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public final class AdornBookItem extends Item {
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
        var bookManager = PlatformBridges.get().getResources().getBookManager();
        if (bookManager.contains(bookId)) {
            textConsumer.accept(Component.translatable("book.byAuthor", bookManager.get(bookId).author()).withStyle(ChatFormatting.GRAY));
        }
    }
}
