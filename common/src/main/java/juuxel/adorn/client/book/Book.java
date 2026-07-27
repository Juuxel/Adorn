package juuxel.adorn.client.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public record Book(Component title, Component subtitle, Component author, List<Page> pages, float titleScale) {
    public static final Codec<Book> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ComponentSerialization.CODEC.fieldOf("title").forGetter(Book::title),
        ComponentSerialization.CODEC.fieldOf("subtitle").forGetter(Book::subtitle),
        ComponentSerialization.CODEC.fieldOf("author").forGetter(Book::author),
        Page.CODEC.listOf().fieldOf("pages").forGetter(Book::pages),
        Codec.FLOAT.fieldOf("titleScale").forGetter(Book::titleScale)
    ).apply(instance, Book::new));

    public static Builder builder() {
        return new Builder();
    }

    public static MutableComponent jumpToPage(MutableComponent text, int page) {
        return text.withStyle(ChatFormatting.UNDERLINE)
            .withStyle(style -> style
                .withClickEvent(new ClickEvent.ChangePage(page))
                .withHoverEvent(new HoverEvent.ShowText(
                    Component.translatable("guide.adorn.contents.jump_to_page", page)
                        .withStyle(ChatFormatting.ITALIC)
                )));
    }

    public static final class Builder {
        private @Nullable Component title;
        private @Nullable Component subtitle;
        private @Nullable Component author;
        private final List<Page> pages = new ArrayList<>();
        private float titleScale = 1f;

        private Builder() {
        }

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(Component subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder author(Component author) {
            this.author = author;
            return this;
        }

        public Builder page(Page page) {
            pages.add(page);
            return this;
        }

        public Builder pageTree(UnaryOperator<PageTreeBuilder> builderOp) {
            // offset 2 = title + 1-indexed pages
            pages.addAll(builderOp.apply(new PageTreeBuilder(pages.size() + 2)).build());
            return this;
        }

        public Builder titleScale(float titleScale) {
            this.titleScale = titleScale;
            return this;
        }

        public Book build() {
            Objects.requireNonNull(title);
            Objects.requireNonNull(subtitle);
            Objects.requireNonNull(author);
            return new Book(title, subtitle, author, List.copyOf(pages), titleScale);
        }
    }
}
