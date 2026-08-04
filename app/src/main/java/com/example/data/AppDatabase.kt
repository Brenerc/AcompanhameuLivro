package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "happy_reading_db"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Pre-populate with initial books
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                populateInitialData(database.bookDao())
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(dao: BookDao) {
            val initialBooks = listOf(
                Book(
                    title = "Hábitos Atômicos",
                    author = "James Clear",
                    totalPages = 320,
                    currentPage = 210,
                    status = ReadingStatus.READING.name,
                    coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuC21vYoykP56lJ339reK3Pb5NPh08UqM-uRawtE7HDrcl5exLj7ZsYX7Sgt6N2KssA7W1gz4qlVVIX2SWKp0VxtayDzz1r_LziDbCuPHrqbimOdEMpGFhIQ-D7CthZMU4A6K0XKAGCGNSjJFbQpRVhNeBGpJpkn2EKvbj2Ms0ZSkuip4iyXnqZzg21j2NHDQojgJMD5hj1Em-km-InUFSyGC_ATfeDkmUCXCG4gioiSjh7Wx5vIGzpb",
                    category = "Desenvolvimento Pessoal",
                    rating = 4.9f,
                    notes = "\"Você não se eleva ao nível dos seus objetivos. Você cai ao nível dos seus sistemas.\""
                ),
                Book(
                    title = "O Circo da Noite",
                    author = "Erin Morgenstern",
                    totalPages = 380,
                    currentPage = 45,
                    status = ReadingStatus.READING.name,
                    coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCu55WEDoGlQ6PBF0Flw1UOAREyFYtOC9in68LJxfUFwxTQom4eWu56YXqjdMBI81jeZo30wAFHWGbtQQhHFhJ6o20gp9mBopuL6gRmUUCYu2QFuNrAYVabpM22YUkVUXd3EZAakTel1kAbaHMZAYG-JbNM0R_BBuYIKCjLPGQYCys23tFmIrFQoSUOFryl0wD6d9Cqi3pagJ3swkbDQ0ULPOSAORSHR-jIUfQ6B0048XwYqW9nJrpG",
                    category = "Fantasia",
                    rating = 4.7f,
                    notes = "\"O circo chega sem avisar. Não há anúncios nem cartazes nas paredes.\""
                ),
                Book(
                    title = "Devoradores de Estrelas",
                    author = "Andy Weir",
                    totalPages = 480,
                    currentPage = 0,
                    status = ReadingStatus.WANT_TO_READ.name,
                    coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAKz-9UHGRQX9C-QyRDAGq03evGIZCwD6XGoIbWVMN73pB7zmyL6834ZwnBxXhsZ-X9a0p3PmSXhTFrs8446QKe-zmjCCaignfhEbZ6pQfuzlMO9h95RhK-AlxfkMrP1SkD9Mr5x1ZdcpCfHiULtEG7GhVkuKTwmsy8LWbVfvkggeoBW8we7_UXoqAvyt-Yhqs0r5G8zXJKo8PDiouakLZLMhvz2-mgmcOzPUdsr7Fs95pBw26lcVK6",
                    category = "Ficção Científica",
                    rating = 4.8f,
                    notes = ""
                ),
                Book(
                    title = "Circe",
                    author = "Madeline Miller",
                    totalPages = 360,
                    currentPage = 0,
                    status = ReadingStatus.WANT_TO_READ.name,
                    coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAVO9wPs4aovzwwWw6ZqOi6JwiTSvyL-SXyfNH8BhX5xQfRPxkf7kXaKFOJft94jY7M8-5Oav2AOMHSFZ-3q8cGTm9XLSd0vhq3aFqjhHBao5ih5dVI8EXrQ2ORyqIOsDoVTKOGNFJ9ka973OtiQMdj8uwEnU6ZIlgtRq8gFfWPMNIMuCF2l2PMOnUmQfc7dAUK96PZv5h0MzQ26NawjykAhRD9lPiR8R7qZKq5zcsI6dllJPqUQEP1",
                    category = "Mitologia / Fantasia",
                    rating = 4.6f,
                    notes = ""
                ),
                Book(
                    title = "O Mistério da Estrela Rosa",
                    author = "Beatriz Sparkles",
                    totalPages = 234,
                    currentPage = 152,
                    status = ReadingStatus.READING.name,
                    coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD_aXKpD3-8d08BP19BJ8aYwLxkZprIFU__uTC7FfS71EapLC44lpexKbtJXSgJDA-q7WgRGIMNahpc4xDK2NLnNfbzSAZ8epytrVdzBjjMkhABV9Dvy-KJuWxPM4lYHSiGoJ2jOCsCpisRfJVI8AAQ1Kxei9fF3aPgyBzd2_cBe85otDpo2PtsPipdKiWjJQOD6CvBCIoN6c1ulGIV9jB5sfKrsOOaQ0TYdq45wjsRi1rNVVMl0_ns",
                    category = "Fantasia Mágica",
                    rating = 4.8f,
                    notes = "\"A estrela rosa não brilha apenas para quem vê, mas para quem sente o calor de sua luz eterna...\""
                ),
                Book(
                    title = "Duna",
                    author = "Frank Herbert",
                    totalPages = 680,
                    currentPage = 680,
                    status = ReadingStatus.READ.name,
                    coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDS0X-K_kpBQWXxMi1g0srbgaHV0biRsQxGwcwkPR_ZP0U1oEImdjcRw8lqsAh5PjjfdYQ8jRq7ZUlvg736AX6a9T-wwHQKMcI6njAW8gldehF_sMwUY_grlEUvvbCp6Gs7pxIgdr93kJ9JXjxLBEiOh-1449pi8krn2r5fRYsR0kpNXB_tFFk8XYV8XimovOHlz8hlRFCRKssLeBJ3N4JI5Cw-B-WY70gCi9FhYxtvI2Ph7eKn4pHW",
                    category = "Ficção Científica",
                    rating = 5.0f,
                    notes = "\"Não devo ter medo. O medo é o assassino da mente.\""
                ),
                Book(
                    title = "1984",
                    author = "George Orwell",
                    totalPages = 328,
                    currentPage = 328,
                    status = ReadingStatus.READ.name,
                    coverUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBCwS8RFLvld7bIQsxA6qUn3rX7yrU1OdeHhnF9LD2SBTICO8IPHE8jsC40HSZ_fKkXDh0KWoV4nSWnI9KdnhHbFooPWhTT2IJGPRToPftDhb_gKRX1Zs8P72yaKQDkJHwLetSSdelSywuUfb7lXbtKiOnFXRp2wu3Lhc6vqaHU32muHIfvPpsHrEeNvyRJ7IA7d1_KF98cUykz_KtyVIIu5wO1V7epilfC5EImqa6uA58SmjOOFJvV",
                    category = "Distopia",
                    rating = 4.5f,
                    notes = "\"Quem controla o passado controla o futuro. Quem controla o presente controla o passado.\""
                )
            )

            initialBooks.forEach { dao.insertBook(it) }
        }
    }
}
