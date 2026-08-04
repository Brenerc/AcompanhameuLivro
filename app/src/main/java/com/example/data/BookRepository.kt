package com.example.data

import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    fun getBookById(id: Long): Flow<Book?> = bookDao.getBookById(id)

    suspend fun getBookByIdSync(id: Long): Book? = bookDao.getBookByIdSync(id)

    fun getBooksByStatus(status: String): Flow<List<Book>> = bookDao.getBooksByStatus(status)

    fun searchBooks(query: String): Flow<List<Book>> = bookDao.searchBooks(query)

    suspend fun insert(book: Book): Long = bookDao.insertBook(book)

    suspend fun update(book: Book) = bookDao.updateBook(book)

    suspend fun updatePageProgress(id: Long, currentPage: Int) {
        val book = bookDao.getBookByIdSync(id)
        if (book != null) {
            val validPage = currentPage.coerceIn(0, book.totalPages)
            // Auto-update status to READ if reached last page
            val newStatus = if (validPage >= book.totalPages && book.totalPages > 0) {
                ReadingStatus.READ.name
            } else if (validPage > 0 && book.status == ReadingStatus.WANT_TO_READ.name) {
                ReadingStatus.READING.name
            } else {
                book.status
            }
            val updatedBook = book.copy(
                currentPage = validPage,
                status = newStatus,
                updatedAt = System.currentTimeMillis()
            )
            bookDao.updateBook(updatedBook)
        }
    }

    suspend fun delete(book: Book) = bookDao.deleteBook(book)

    suspend fun deleteById(id: Long) = bookDao.deleteBookById(id)
}
