package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.client.book.Book;
import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.book.Image;
import juuxel.adorn.client.book.Page;
import juuxel.adorn.item.BookKey;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.util.Vec2i;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public final class BookGenerator extends FabricCodecDataProvider<Book> {
    private static final String DIRECTORY = BookManager.DATA_TYPE;

    public BookGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture, DataOutput.OutputType.RESOURCE_PACK, DIRECTORY, Book.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Book> consumer, RegistryWrapper.WrapperLookup lookup) {
        consumer.accept(
            AdornCommon.id("guide"),
            Book.builder()
                .title(Text.literal("Adorn"))
                .titleScale(1.5f)
                .subtitle(BookKey.GUIDE.createStack().getItemName().copy().formatted(Formatting.ITALIC))
                .author(Text.translatable("item.adorn.guide_book.author"))
                .pageTree(pageTree -> pageTree
                    .page(Page.builder()
                        .icon(AdornTags.TABLES.item())
                        .title(Text.translatable("guide.adorn.topic.table.title"))
                        .content(Text.translatable("guide.adorn.topic.table.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.CHAIRS.item())
                        .title(Text.translatable("guide.adorn.topic.chair.title"))
                        .content(Text.translatable("guide.adorn.topic.chair.text"))
                        .build())
                    .sublevel()
                    .page(Page.builder()
                        .icon(AdornTags.POSTS.item())
                        .title(Text.translatable("guide.adorn.topic.post.title"))
                        .content(Text.translatable("guide.adorn.topic.post.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.PLATFORMS.item())
                        .title(Text.translatable("guide.adorn.topic.platform.title"))
                        .content(Text.translatable("guide.adorn.topic.platform.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.STEPS.item())
                        .title(Text.translatable("guide.adorn.topic.step.title"))
                        .content(Text.translatable("guide.adorn.topic.step.text"))
                        .build())
                    .end(content -> Page.builder()
                        .icon(AdornTags.POSTS.item())
                        .icon(AdornTags.STEPS.item())
                        .icon(AdornTags.PLATFORMS.item())
                        .title(Text.translatable("guide.adorn.topic.building_blocks.title"))
                        .content(content)
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.KITCHEN_COUNTERS.item())
                        .icon(AdornTags.KITCHEN_CUPBOARDS.item())
                        .title(Text.translatable("guide.adorn.topic.kitchen.title"))
                        .content(Text.translatable("guide.adorn.topic.kitchen.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.KITCHEN_SINKS.item())
                        .title(Text.translatable("guide.adorn.topic.kitchen_sink.title"))
                        .content(Text.translatable("guide.adorn.topic.kitchen_sink.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.SOFAS.item())
                        .title(Text.translatable("guide.adorn.topic.sofa.title"))
                        .content(Text.translatable("guide.adorn.topic.sofa.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornBlocks.TRADING_STATION.get())
                        .title(Text.translatable("guide.adorn.topic.trading_station.title"))
                        .content(Text.translatable("guide.adorn.topic.trading_station.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.SHELVES.item())
                        .title(Text.translatable("guide.adorn.topic.shelf.title"))
                        .content(Text.translatable("guide.adorn.topic.shelf.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.DRAWERS.item())
                        .title(Text.translatable("guide.adorn.topic.drawer.title"))
                        .content(Text.translatable("guide.adorn.topic.drawer.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.REGULAR_CHIMNEYS.item())
                        .title(Text.translatable("guide.adorn.topic.chimney.title"))
                        .content(Text.translatable("guide.adorn.topic.chimney.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.PRISMARINE_CHIMNEYS.item())
                        .title(Text.translatable("guide.adorn.topic.prismarine_chimney.title"))
                        .content(Text.translatable("guide.adorn.topic.prismarine_chimney.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornBlocks.BREWER.get())
                        .title(Text.translatable("guide.adorn.topic.brewer.title"))
                        .content(Text.translatable("guide.adorn.topic.brewer.text"))
                        .image(new Image(
                            AdornCommon.id("textures/gui/brewer_guide.png"),
                            new Vec2i(113, 61),
                            Image.Placement.AFTER_TEXT,
                            List.of(
                                new Image.HoverArea(
                                    new Vec2i(1, 1),
                                    new Vec2i(16, 16),
                                    Text.translatable("guide.adorn.topic.brewer.input_slot")
                                ),
                                new Image.HoverArea(
                                    new Vec2i(61, 1),
                                    new Vec2i(16, 16),
                                    Text.translatable("guide.adorn.topic.brewer.input_slot")
                                ),
                                new Image.HoverArea(
                                    new Vec2i(27, 36),
                                    new Vec2i(24, 24),
                                    Text.translatable("guide.adorn.topic.brewer.mug_slot")
                                ),
                                new Image.HoverArea(
                                    new Vec2i(74, 44),
                                    new Vec2i(16, 16),
                                    Text.translatable("guide.adorn.topic.brewer.tank_slot")
                                ),
                                new Image.HoverArea(
                                    new Vec2i(96, 1),
                                    new Vec2i(16, 59),
                                    Text.translatable("guide.adorn.topic.brewer.tank")
                                )
                            )
                        ))
                        .build())
                    .end(body -> Page.builder()
                        .icon(Items.BOOK)
                        .title(Text.translatable("guide.adorn.contents.title"))
                        .content(body)
                        .build())
                )
                .build()
        );
        consumer.accept(
            AdornCommon.id("traders_manual"),
            Book.builder()
                .title(BookKey.TRADERS_MANUAL.createStack().getItemName().copy().formatted(Formatting.UNDERLINE, Formatting.BOLD))
                .subtitle(Text.translatable("item.adorn.traders_manual.subtitle").formatted(Formatting.ITALIC))
                .author(Text.translatable("item.adorn.traders_manual.author"))
                .page(Page.builder()
                    .icon(Items.EMERALD)
                    .title(Text.translatable("item.adorn.traders_manual.buying.title"))
                    .content(Text.translatable("item.adorn.traders_manual.buying.text"))
                    .build())
                .page(Page.builder()
                    .icon(Items.GOLD_INGOT)
                    .title(Text.translatable("item.adorn.traders_manual.selling.title"))
                    .content(Text.translatable("item.adorn.traders_manual.selling.text"))
                    .build())
                .build()
        );
    }

    @Override
    public String getName() {
        return "Books";
    }
}
