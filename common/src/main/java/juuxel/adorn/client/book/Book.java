package juuxel.adorn.client.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public record Book(Text title, Text subtitle, Text author, List<Page> pages, float titleScale) {
    public static final Codec<Book> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TextCodecs.CODEC.fieldOf("title").forGetter(Book::title),
        TextCodecs.CODEC.fieldOf("subtitle").forGetter(Book::subtitle),
        TextCodecs.CODEC.fieldOf("author").forGetter(Book::author),
        Page.CODEC.listOf().fieldOf("pages").forGetter(Book::pages),
        Codec.FLOAT.fieldOf("titleScale").forGetter(Book::titleScale)
    ).apply(instance, Book::new));

    public static Builder builder() {
        return new Builder();
    }

    public static MutableText jumpToPage(MutableText text, int page) {
        return text.formatted(Formatting.UNDERLINE)
            .styled(style -> style
                .withClickEvent(new ClickEvent.ChangePage(page))
                .withHoverEvent(new HoverEvent.ShowText(
                    Text.translatable("guide.adorn.contents.jump_to_page", page)
                        .formatted(Formatting.ITALIC)
                )));
    }

    public static final class Builder {
        private @Nullable Text title;
        private @Nullable Text subtitle;
        private @Nullable Text author;
        private final List<Page> pages = new ArrayList<>();
        private float titleScale = 1f;

        private Builder() {
        }

        public Builder title(Text title) {
            this.title = title;
            return this;
        }

        public Builder subtitle(Text subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder author(Text author) {
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
