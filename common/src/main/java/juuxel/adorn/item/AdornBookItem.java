package juuxel.adorn.item;

import juuxel.adorn.networking.OpenBookS2CMessage;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.function.Consumer;

public final class AdornBookItem extends Item {
    private final Identifier bookId;

    public AdornBookItem(Identifier bookId, Settings settings) {
        super(settings);
        this.bookId = bookId;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()) {
            PlatformBridges.get().getNetwork().sendToClient(user, new OpenBookS2CMessage(bookId));
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        var bookManager = PlatformBridges.get().getResources().getBookManager();
        if (bookManager.contains(bookId)) {
            textConsumer.accept(Text.translatable("book.byAuthor", bookManager.get(bookId).author()).formatted(Formatting.GRAY));
        }
    }
}
