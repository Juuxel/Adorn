package juuxel.adorn.item;

import com.mojang.serialization.Codec;
import juuxel.adorn.AdornCommon;
import juuxel.adorn.component.AdornComponentTypes;
import juuxel.adorn.platform.PlatformBridges;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public record BookKey(Identifier id) implements TooltipAppender {
    public static final Codec<BookKey> CODEC = Identifier.CODEC.xmap(BookKey::new, BookKey::id);

    public static final BookKey GUIDE = of("guide");
    public static final BookKey TRADERS_MANUAL = of("traders_manual");

    private static BookKey of(String id) {
        return new BookKey(AdornCommon.id(id));
    }

    public Identifier getItemId() {
        return this.equals(GUIDE) ? AdornCommon.id("guide_book") : id;
    }

    public ItemStack createStack() {
        var stack = new ItemStack(AdornItems.GUIDE_BOOK.get());
        stack.set(AdornComponentTypes.BOOK.get(), this);
        return stack;
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        var bookManager = PlatformBridges.get().getResources().getBookManager();
        if (bookManager.contains(id)) {
            textConsumer.accept(Text.translatable("book.byAuthor", bookManager.get(id).author()).formatted(Formatting.GRAY));
        }
    }
}
