package juuxel.adorn.gui.widget

interface PageContainer {
    val currentPage: Int // 0-indexed
    val pageCount: Int

    fun hasPreviousPage(): Boolean
    fun hasNextPage(): Boolean

    fun showPreviousPage()
    fun showNextPage()
}
