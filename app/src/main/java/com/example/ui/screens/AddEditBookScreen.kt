package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Book
import com.example.data.ReadingStatus
import com.example.ui.components.BookCoverImage
import com.example.ui.theme.PinkPrimary
import com.example.ui.theme.StarGold
import com.example.ui.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookScreen(
    bookId: Long?,
    viewModel: BookViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isEditMode = bookId != null && bookId != 0L

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var totalPagesText by remember { mutableStateOf("300") }
    var currentPageText by remember { mutableStateOf("0") }
    var selectedStatus by remember { mutableStateOf(ReadingStatus.WANT_TO_READ) }
    var category by remember { mutableStateOf("Fantasia") }
    var coverUrl by remember { mutableStateOf("") }
    var rating by remember { mutableFloatStateOf(4.5f) }
    var notes by remember { mutableStateOf("") }

    if (isEditMode) {
        val bookState = viewModel.getBookById(bookId!!).collectAsStateWithLifecycle()
        val book = bookState.value
        LaunchedEffect(book) {
            if (book != null) {
                title = book.title
                author = book.author
                totalPagesText = book.totalPages.toString()
                currentPageText = book.currentPage.toString()
                selectedStatus = book.statusEnum
                category = book.category
                coverUrl = book.coverUrl
                rating = book.rating
                notes = book.notes
            }
        }
    }

    val sampleCovers = listOf(
        "https://lh3.googleusercontent.com/aida-public/AB6AXuC21vYoykP56lJ339reK3Pb5NPh08UqM-uRawtE7HDrcl5exLj7ZsYX7Sgt6N2KssA7W1gz4qlVVIX2SWKp0VxtayDzz1r_LziDbCuPHrqbimOdEMpGFhIQ-D7CthZMU4A6K0XKAGCGNSjJFbQpRVhNeBGpJpkn2EKvbj2Ms0ZSkuip4iyXnqZzg21j2NHDQojgJMD5hj1Em-km-InUFSyGC_ATfeDkmUCXCG4gioiSjh7Wx5vIGzpb",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCu55WEDoGlQ6PBF0Flw1UOAREyFYtOC9in68LJxfUFwxTQom4eWu56YXqjdMBI81jeZo30wAFHWGbtQQhHFhJ6o20gp9mBopuL6gRmUUCYu2QFuNrAYVabpM22YUkVUXd3EZAakTel1kAbaHMZAYG-JbNM0R_BBuYIKCjLPGQYCys23tFmIrFQoSUOFryl0wD6d9Cqi3pagJ3swkbDQ0ULPOSAORSHR-jIUfQ6B0048XwYqW9nJrpG",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAKz-9UHGRQX9C-QyRDAGq03evGIZCwD6XGoIbWVMN73pB7zmyL6834ZwnBxXhsZ-X9a0p3PmSXhTFrs8446QKe-zmjCCaignfhEbZ6pQfuzlMO9h95RhK-AlxfkMrP1SkD9Mr5x1ZdcpCfHiULtEG7GhVkuKTwmsy8LWbVfvkggeoBW8we7_UXoqAvyt-Yhqs0r5G8zXJKo8PDiouakLZLMhvz2-mgmcOzPUdsr7Fs95pBw26lcVK6",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAVO9wPs4aovzwwWw6ZqOi6JwiTSvyL-SXyfNH8BhX5xQfRPxkf7kXaKFOJft94jY7M8-5Oav2AOMHSFZ-3q8cGTm9XLSd0vhq3aFqjhHBao5ih5dVI8EXrQ2ORyqIOsDoVTKOGNFJ9ka973OtiQMdj8uwEnU6ZIlgtRq8gFfWPMNIMuCF2l2PMOnUmQfc7dAUK96PZv5h0MzQ26NawjykAhRD9lPiR8R7qZKq5zcsI6dllJPqUQEP1"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "Editar Livro" else "Novo Livro",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("btn_back_add_edit")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cover Image Preview & Selector
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BookCoverImage(
                        coverUrl = coverUrl,
                        title = title,
                        modifier = Modifier
                            .width(110.dp)
                            .height(160.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = PinkPrimary),
                        cornerRadius = 16f
                    )

                    OutlinedTextField(
                        value = coverUrl,
                        onValueChange = { coverUrl = it },
                        label = { Text("Link da Imagem / URL da Capa") },
                        placeholder = { Text("https://exemplo.com/capa.jpg") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_cover_url"),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                    )

                    Text(
                        text = "Ou escolha uma imagem de exemplo:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(sampleCovers) { url ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { coverUrl = url }
                            ) {
                                BookCoverImage(
                                    coverUrl = url,
                                    title = "",
                                    modifier = Modifier.fillMaxSize(),
                                    cornerRadius = 8f
                                )
                            }
                        }
                    }
                }
            }

            // Input Fields
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título do Livro *") },
                placeholder = { Text("Ex: O Nome do Vento") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_title"),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Autor *") },
                placeholder = { Text("Ex: Patrick Rothfuss") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_author"),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = totalPagesText,
                    onValueChange = { totalPagesText = it.filter { char -> char.isDigit() } },
                    label = { Text("Total de Páginas *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_total_pages"),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                OutlinedTextField(
                    value = currentPageText,
                    onValueChange = { currentPageText = it.filter { char -> char.isDigit() } },
                    label = { Text("Página Atual") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_add_current_page"),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
            }

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Gênero / Categoria") },
                placeholder = { Text("Ex: Fantasia, Ficção, Romance") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_category"),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            // Status Selector Pills
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Status da Leitura",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        )
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ReadingStatus.entries.forEach { status ->
                        val isSelected = selectedStatus == status
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .clickable { selectedStatus = status }
                                .testTag("pill_status_${status.name.lowercase()}"),
                            shape = CircleShape,
                            color = if (isSelected) PinkPrimary else Color.Transparent
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = status.labelPt,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Rating Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Sua Avaliação",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        val starIndex = index + 1
                        IconButton(
                            onClick = { rating = starIndex.toFloat() },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$starIndex estrelas",
                                tint = if (starIndex <= rating.toInt()) StarGold else MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            // Notes Text Field
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Anotações ou Citações Favoritas") },
                placeholder = { Text("Escreva trechos do livro ou suas impressões...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_notes"),
                maxLines = 4,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "Por favor, digite o título do livro", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val totalPages = totalPagesText.toIntOrNull() ?: 1
                    val currentPage = currentPageText.toIntOrNull() ?: 0

                    viewModel.saveBook(
                        id = bookId ?: 0L,
                        title = title,
                        author = author,
                        totalPages = totalPages,
                        currentPage = currentPage,
                        status = selectedStatus,
                        coverUrl = coverUrl,
                        category = category,
                        rating = rating,
                        notes = notes
                    )

                    Toast.makeText(
                        context,
                        if (isEditMode) "Livro atualizado!" else "Livro adicionado!",
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("btn_save_book"),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PinkPrimary,
                    contentColor = Color.White
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Salvar Livro",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
            }

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = CircleShape
            ) {
                Text(
                    text = "Cancelar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
