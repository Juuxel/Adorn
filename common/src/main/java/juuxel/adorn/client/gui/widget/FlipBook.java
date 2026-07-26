package juuxel.adorn.client.gui.widget;

import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.ArrayList;
import java.util.List;

public final class FlipBook extends WidgetEnvelope implements PageContainer<GuiEventListener> {
    private final List<GuiEventListener> pages = new ArrayList<>();
    private final Runnable pageUpdateListener;
    private int currentPage = 0;

    public FlipBook(Runnable pageUpdateListener) {
        this.pageUpdateListener = pageUpdateListener;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        pageUpdateListener.run();
    }

    @Override
    public int getPageCount() {
        return pages.size();
    }

    @Override
    public GuiEventListener getCurrentPageValue() {
        return current();
    }

    @Override
    protected GuiEventListener current() {
        return pages.get(currentPage);
    }

    public void add(GuiEventListener page) {
        pages.add(page);
    }

    @Override
    public void showPreviousPage() {
        if (hasPreviousPage()) {
            currentPage--;
            pageUpdateListener.run();
        }
    }

    @Override
    public void showNextPage() {
        if (hasNextPage()) {
            currentPage++;
            pageUpdateListener.run();
        }
    }
}
