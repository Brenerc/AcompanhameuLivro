package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Book
import com.example.data.BookRepository
import com.example.data.ReadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = BookRepository(database.bookDao())
    }

    val allBooks: StateFlow<List<Book>> = repository.allBooks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow<String?>(null) // null = all
    val selectedStatusFilter: StateFlow<String?> = _selectedStatusFilter.asStateFlow()

    val filteredBooks: StateFlow<List<Book>> = combine(
        allBooks,
        searchQuery,
        selectedStatusFilter
    ) { books, query, statusFilter ->
        books.filter { book ->
            val matchesQuery = query.isBlank() ||
                    book.title.contains(query, ignoreCase = true) ||
                    book.author.contains(query, ignoreCase = true) ||
                    book.category.contains(query, ignoreCase = true)

            val matchesStatus = statusFilter == null || book.status.equals(statusFilter, ignoreCase = true)

            matchesQuery && matchesStatus
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val readingBooks: StateFlow<List<Book>> = combine(allBooks) { booksList ->
        booksList[0].filter { it.statusEnum == ReadingStatus.READING }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wantToReadBooks: StateFlow<List<Book>> = combine(allBooks) { booksList ->
        booksList[0].filter { it.statusEnum == ReadingStatus.WANT_TO_READ }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val readBooks: StateFlow<List<Book>> = combine(allBooks) { booksList ->
        booksList[0].filter { it.statusEnum == ReadingStatus.READ }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onStatusFilterChanged(status: String?) {
        _selectedStatusFilter.value = status
    }

    fun getBookById(id: Long): StateFlow<Book?> {
        val bookFlow = MutableStateFlow<Book?>(null)
        viewModelScope.launch {
            repository.getBookById(id).collect {
                bookFlow.value = it
            }
        }
        return bookFlow
    }

    fun updatePageProgress(bookId: Long, newPage: Int) {
        viewModelScope.launch {
            repository.updatePageProgress(bookId, newPage)
        }
    }

    fun saveBook(
        id: Long = 0,
        title: String,
        author: String,
        totalPages: Int,
        currentPage: Int,
        status: ReadingStatus,
        coverUrl: String,
        category: String,
        rating: Float,
        notes: String
    ) {
        viewModelScope.launch {
            val validTotal = totalPages.coerceAtLeast(1)
            val validCurrent = currentPage.coerceIn(0, validTotal)
            val autoStatus = if (validCurrent >= validTotal) ReadingStatus.READ else status

            val book = Book(
                id = id,
                title = title.ifBlank { "Sem Título" },
                author = author.ifBlank { "Autor Desconhecido" },
                totalPages = validTotal,
                currentPage = validCurrent,
                status = autoStatus.name,
                coverUrl = coverUrl.trim(),
                category = category.ifBlank { "Geral" },
                rating = rating.coerceIn(0f, 5f),
                notes = notes,
                updatedAt = System.currentTimeMillis()
            )

            if (id == 0L) {
                repository.insert(book)
            } else {
                repository.update(book)
            }
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.delete(book)
        }
    }

    fun updateBookNotes(bookId: Long, newNote: String) {
        viewModelScope.launch {
            val book = repository.getBookByIdSync(bookId)
            if (book != null) {
                val updated = book.copy(
                    notes = newNote,
                    updatedAt = System.currentTimeMillis()
                )
                repository.update(updated)
            }
        }
    }
}
