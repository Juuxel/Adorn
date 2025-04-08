package juuxel.adorn.item;

import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.networking.OpenBookS2CMessage;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public final class AdornBookItem extends Item {
    public AdornBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        var key = stack.getOrDefault(AdornComponentTypes.BOOK.get(), BookKey.GUIDE);
        return Text.translatable(Util.createTranslationKey("item", key.getItemId()));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            var stack = user.getStackInHand(hand);
            var bookId = stack.getOrDefault(AdornComponentTypes.BOOK.get(), BookKey.GUIDE).id();
            PlatformBridges.get().getNetwork().sendToClient(user, new OpenBookS2CMessage(bookId));
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return ActionResult.SUCCESS;
    }
}
