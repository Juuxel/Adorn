package juuxel.adorn.data;

import juuxel.adorn.AdornCommon;
import juuxel.adorn.block.AdornBlocks;
import juuxel.adorn.client.book.Book;
import juuxel.adorn.client.book.BookManager;
import juuxel.adorn.client.book.Image;
import juuxel.adorn.client.book.Page;
import juuxel.adorn.item.AdornItems;
import juuxel.adorn.lib.AdornTags;
import juuxel.adorn.lib.registry.Registered;
import juuxel.adorn.util.Vec2i;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public final class BookGenerator extends FabricCodecDataProvider<Book> {
    private static final String DIRECTORY = BookManager.DATA_TYPE;

    public BookGenerator(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, DIRECTORY, Book.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, Book> consumer, HolderLookup.Provider lookup) {
        consumer.accept(
            AdornCommon.id("guide"),
            Book.builder()
                .title(Component.literal("Adorn"))
                .titleScale(1.5f)
                .subtitle(getItemName(AdornItems.GUIDE_BOOK).withStyle(ChatFormatting.ITALIC))
                .author(Component.translatable("item.adorn.guide_book.author"))
                .pageTree(pageTree -> pageTree
                    .page(Page.builder()
                        .icon(AdornTags.TABLES.item())
                        .title(Component.translatable("guide.adorn.topic.table.title"))
                        .content(Component.translatable("guide.adorn.topic.table.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.CHAIRS.item())
                        .title(Component.translatable("guide.adorn.topic.chair.title"))
                        .content(Component.translatable("guide.adorn.topic.chair.text"))
                        .build())
                    .sublevel()
                    .page(Page.builder()
                        .icon(AdornTags.POSTS.item())
                        .title(Component.translatable("guide.adorn.topic.post.title"))
                        .content(Component.translatable("guide.adorn.topic.post.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.PLATFORMS.item())
                        .title(Component.translatable("guide.adorn.topic.platform.title"))
                        .content(Component.translatable("guide.adorn.topic.platform.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.STEPS.item())
                        .title(Component.translatable("guide.adorn.topic.step.title"))
                        .content(Component.translatable("guide.adorn.topic.step.text"))
                        .build())
                    .end(content -> Page.builder()
                        .icon(AdornTags.POSTS.item())
                        .icon(AdornTags.STEPS.item())
                        .icon(AdornTags.PLATFORMS.item())
                        .title(Component.translatable("guide.adorn.topic.building_blocks.title"))
                        .content(content)
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.KITCHEN_COUNTERS.item())
                        .icon(AdornTags.KITCHEN_CUPBOARDS.item())
                        .title(Component.translatable("guide.adorn.topic.kitchen.title"))
                        .content(Component.translatable("guide.adorn.topic.kitchen.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.KITCHEN_SINKS.item())
                        .title(Component.translatable("guide.adorn.topic.kitchen_sink.title"))
                        .content(Component.translatable("guide.adorn.topic.kitchen_sink.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.SOFAS.item())
                        .title(Component.translatable("guide.adorn.topic.sofa.title"))
                        .content(Component.translatable("guide.adorn.topic.sofa.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornBlocks.TRADING_STATION.get())
                        .title(Component.translatable("guide.adorn.topic.trading_station.title"))
                        .content(Component.translatable("guide.adorn.topic.trading_station.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.SHELVES.item())
                        .title(Component.translatable("guide.adorn.topic.shelf.title"))
                        .content(Component.translatable("guide.adorn.topic.shelf.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.DRAWERS.item())
                        .title(Component.translatable("guide.adorn.topic.drawer.title"))
                        .content(Component.translatable("guide.adorn.topic.drawer.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.REGULAR_CHIMNEYS.item())
                        .title(Component.translatable("guide.adorn.topic.chimney.title"))
                        .content(Component.translatable("guide.adorn.topic.chimney.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornTags.PRISMARINE_CHIMNEYS.item())
                        .title(Component.translatable("guide.adorn.topic.prismarine_chimney.title"))
                        .content(Component.translatable("guide.adorn.topic.prismarine_chimney.text"))
                        .build())
                    .page(Page.builder()
                        .icon(AdornBlocks.BREWER.get())
                        .title(Component.translatable("guide.adorn.topic.brewer.title"))
                        .content(Component.translatable("guide.adorn.topic.brewer.text"))
                        .image(new Image(
                            AdornCommon.id("textures/gui/brewer_guide.png"),
                            new Vec2i(113, 61),
                            Image.Placement.AFTER_TEXT,
                            List.of(
                                new Image.HoverArea(
                                    new Vec2i(1, 1),
                                    new Vec2i(16, 16),
                                    Component.translatable("guide.adorn.topic.brewer.input_slot")
                                ),
                                new Image.HoverArea(
                                    new Vec2i(61, 1),
                                    new Vec2i(16, 16),
                                    Component.translatable("guide.adorn.topic.brewer.input_slot")
                                ),
                                new Image.HoverArea(
                                    new Vec2i(27, 36),
                                    new Vec2i(24, 24),
                                    Component.translatable("guide.adorn.topic.brewer.mug_slot")
                                ),
                                new Image.HoverArea(
                                    new Vec2i(74, 44),
                                    new Vec2i(16, 16),
                                    Component.translatable("guide.adorn.topic.brewer.tank_slot")
                                ),
                                new Image.HoverArea(
                                    new Vec2i(96, 1),
                                    new Vec2i(16, 59),
                                    Component.translatable("guide.adorn.topic.brewer.tank")
                                )
                            )
                        ))
                        .build())
                    .end(body -> Page.builder()
                        .icon(Items.BOOK)
                        .title(Component.translatable("guide.adorn.contents.title"))
                        .content(body)
                        .build())
                )
                .build()
        );
        consumer.accept(
            AdornCommon.id("traders_manual"),
            Book.builder()
                .title(getItemName(AdornItems.TRADERS_MANUAL).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD))
                .subtitle(Component.translatable("item.adorn.traders_manual.subtitle").withStyle(ChatFormatting.ITALIC))
                .author(Component.translatable("item.adorn.traders_manual.author"))
                .page(Page.builder()
                    .icon(Items.EMERALD)
                    .title(Component.translatable("item.adorn.traders_manual.buying.title"))
                    .content(Component.translatable("item.adorn.traders_manual.buying.text"))
                    .build())
                .page(Page.builder()
                    .icon(Items.GOLD_INGOT)
                    .title(Component.translatable("item.adorn.traders_manual.selling.title"))
                    .content(Component.translatable("item.adorn.traders_manual.selling.text"))
                    .build())
                .build()
        );
    }

    private static MutableComponent getItemName(Registered<? extends Item> item) {
        return Component.translatable(item.get().getDescriptionId());
    }

    @Override
    public String getName() {
        return "Books";
    }
}
