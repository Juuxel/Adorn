package juuxel.adorn.client.gui.widget;

public interface PageContainer<T> {
    // 0-indexed
    int getCurrentPage();
    void setCurrentPage(int currentPage);

    int getPageCount();
    T getCurrentPageValue();

    default boolean hasPreviousPage() {
        return getCurrentPage() > 0;
    }

    default boolean hasNextPage() {
        return getCurrentPage() < getPageCount() - 1;
    }

    void showPreviousPage();
    void showNextPage();
}
