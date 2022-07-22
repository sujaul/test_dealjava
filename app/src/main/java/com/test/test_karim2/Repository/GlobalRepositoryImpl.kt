package com.test.test_karim2.Repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.test.test_karim2.data.local.AppDatabase
import com.test.test_karim2.data.model.*
import com.test.test_karim2.data.remote.ApiService
import com.test.test_karim2.data.remote.ErrorMessage
import com.test.test_karim2.util.DateOperationUtil
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GlobalRepositoryImpl(
    private val service: ApiService
): globalRepository, KoinComponent {

    private val db: AppDatabase by inject()

    override suspend fun searchBook(tittle: String): Flow<List<BookStokRelation>> {
        var data: Flow<List<BookStokRelation>> = db.bookDAO().filmAndFilmStokByGenre("%$tittle%")
        val dataList = db.bookDAO().filmAndFilmStokByGenreSus("%$tittle%")
        if (dataList.isEmpty()){
            val input = if (tittle == "") "flowers+intitle:flowers" else "$tittle+intitle:$tittle"
            val response = service.search(input).await()
            if (response.isSuccessful){
                try {
                    response.body()?.let {res ->
                        if (res.totalItems > 0){
                            val bookList = ArrayList<Book>()
                            res.items?.forEach {
                                val book = Book()
                                book.id = it.id
                                book.name = it.volumeInfo?.title ?: ""
                                var author = ""
                                it.volumeInfo?.authors?.let { arr ->
                                    arr.forEachIndexed { index, jsonElement ->
                                        author += jsonElement.asString
                                        if (index < arr.size() -1)
                                            author += ", "
                                    }
                                }
                                book.author = author
                                var genre = ""
                                it.volumeInfo?.categories?.let { arr ->
                                    arr.forEachIndexed { index, jsonElement ->
                                        genre += jsonElement.asString
                                        if (index < arr.size() -1)
                                            genre += ", "
                                    }
                                }
                                book.genre = genre
                                book.description = it.searchInfo?.textSnippet ?: ""
                                book.url = it.volumeInfo?.imageLinks?.thumbnail ?: ""
                                book.publisher = it.volumeInfo?.publisher ?: ""
                                book.published_date = it.volumeInfo?.publishedDate ?: ""
                                book.qty = 1 // set by devault
                                db.bookDAO().insertSus(book)
                                val bookStok = BookStok()
                                bookStok.book_id = it.id
                                bookStok.stok_awal = 0
                                bookStok.credit = 1 // set by devault
                                bookStok.debit = 0
                                bookStok.stok_ahir = 1 // set by devault
                                bookStok.created_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
                                bookStok.updated_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
                                db.bookStokDAO().insertSus(bookStok)
                                bookList.add(book)
                            }
                            data = db.bookDAO().filmAndFilmStokByGenre("%$tittle%")
                        }
                    } ?: throw Throwable("The body is null")
                } catch (e: Exception){
                    throw Throwable(e.message)
                }
            } else {
                if (response.errorBody()!=null) {
                    val json: String = response.errorBody()!!.string()
                    val obj: JsonObject = JsonParser().parse(json).asJsonObject
                    val error = Gson().fromJson(obj, ErrorMessage::class.java)
                    throw Throwable(error.reason)
                } else {
                    throw Throwable("The error body is null")
                }
            }
        }
        return data
    }

    override suspend fun addStok(book: Book, stok: Int) {
        val bookStok = BookStok()
        bookStok.book_id = book.id
        bookStok.stok_awal = stok
        bookStok.credit = 1
        bookStok.debit = 0
        bookStok.stok_ahir = stok+1
        bookStok.created_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
        bookStok.updated_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
        db.bookStokDAO().insertSus(bookStok)
    }

    override suspend fun minusStok(book: Book, stok: Int) {
        val bookStok = BookStok()
        bookStok.book_id = book.id
        bookStok.stok_awal = stok
        bookStok.credit = 0
        bookStok.debit = 1
        bookStok.stok_ahir = stok-1
        bookStok.created_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
        bookStok.updated_at = DateOperationUtil.getCurrentTimeStr("yyyy-MM-dd HH:mm:ss")
        db.bookStokDAO().insertSus(bookStok)
    }

    override suspend fun getReport(book_id: String): BookReport {
        val credit = db.bookStokDAO().allByBookIdCredit(book_id)
        val debit = db.bookStokDAO().oneByBookIdDebit(book_id)
        val bookReport = BookReport()
        if (credit.size > 1){
            bookReport.return_date = credit[0].created_at
        } else bookReport.return_date = "No return date"
        bookReport.borrow_date = debit?.created_at ?: "No borrow date"
        return bookReport
    }
}

interface globalRepository{
    suspend fun searchBook(tittle: String): Flow<List<BookStokRelation>>
    suspend fun addStok(book: Book, stok: Int)
    suspend fun minusStok(book: Book, stok: Int)
    suspend fun getReport(book_id: String): BookReport
}