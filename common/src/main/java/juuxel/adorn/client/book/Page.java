package juuxel.adorn.client.book;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.adorn.util.EntryOrTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Page(List<Icon> icons, Component title, Component text, @Nullable Image image) {
    public static final Codec<Page> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Icon.CODEC.listOf().fieldOf("icons").forGetter(Page::icons),
        ComponentSerialization.CODEC.fieldOf("title").forGetter(Page::title),
        ComponentSerialization.CODEC.optionalFieldOf("text", Component.empty()).forGetter(Page::text),
        Image.CODEC.optionalFieldOf("image").forGetter(page -> Optional.ofNullable(page.image))
    ).apply(instance, Page::new));

    // For DFU
    private Page(List<Icon> icons, Component title, Component text, Optional<Image> image) {
        this(icons, title, text, image.orElse(null));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final List<Icon> icons = new ArrayList<>();
        private @Nullable Component title;
        private @Nullable Component text;
        private @Nullable Image image;

        private Builder() {
        }

        public Builder icon(ItemLike item) {
            icons.add(new Icon(new EntryOrTag.OfEntry<>(item.asItem())));
            return this;
        }

        public Builder icon(TagKey<Item> tag) {
            icons.add(new Icon(new EntryOrTag.OfTag<>(tag)));
            return this;
        }

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder content(Component text) {
            this.text = text;
            return this;
        }

        public Builder image(Image image) {
            this.image = image;
            return this;
        }

        public Page build() {
            if (icons.isEmpty()) throw new IllegalArgumentException("Page has no icons");
            Objects.requireNonNull(title);
            Objects.requireNonNull(text);
            return new Page(icons, title, text, image);
        }
    }

    public record Icon(EntryOrTag<Item> items) {
        public static final Codec<Icon> CODEC = EntryOrTag.codec(Registries.ITEM).xmap(Icon::new, Icon::items);

        public List<ItemStack> createStacks() {
            return switch (items) {
                case EntryOrTag.OfEntry(var item) -> List.of(item.getDefaultInstance());
                case EntryOrTag.OfTag(var tag) -> BuiltInRegistries.ITEM.get(tag).map(entries -> {
                    List<ItemStack> result = new ArrayList<>(entries.size());
                    for (var entry : entries) {
                        result.add(entry.value().getDefaultInstance());
                    }
                    return result;
                }).orElse(List.of());
            };
        }
    }
}
