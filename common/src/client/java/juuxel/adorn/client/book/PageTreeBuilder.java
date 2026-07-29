package juuxel.adorn.client.book;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntStack;
import juuxel.adorn.util.TextBuilder;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class PageTreeBuilder {
    private final List<Page> pages = new ArrayList<>();
    private final IntList pageDepths = new IntArrayList();
    private final IntStack levelStartIndices = new IntArrayList();
    private final int offset;
    private int depth = 0;

    public PageTreeBuilder(int offset) {
        this.offset = offset;
        sublevel();
    }

    public PageTreeBuilder sublevel() {
        levelStartIndices.push(pages.size());
        pages.add(null);
        pageDepths.add(depth);
        depth++;
        return this;
    }

    public PageTreeBuilder page(Page page) {
        pages.add(page);
        pageDepths.add(depth);
        return this;
    }

    public PageTreeBuilder end(Function<Component, Page> tocBuilder) {
        depth--;
        int tocIndex = levelStartIndices.popInt();
        TextBuilder contentBuilder = new TextBuilder();
        contentBuilder.add(Component.empty());

        for (int i = tocIndex + 1; i < pages.size(); i++) {
            if (i != tocIndex + 1) contentBuilder.newLine();

            int pageDepth = pageDepths.getInt(i);
            var indent = "  ".repeat(pageDepth - depth - 1);
            if (!indent.isEmpty()) contentBuilder.add(Component.literal(indent));
            contentBuilder.add(Book.jumpToPage(pages.get(i).title().copy(), offset + i));
        }

        Page toc = tocBuilder.apply(contentBuilder.build());
        pages.set(tocIndex, toc);
        return this;
    }

    public List<Page> build() {
        if (depth > 0) {
            throw new IllegalArgumentException("Cannot build page with unfinished tree level");
        }

        return pages;
    }
}
