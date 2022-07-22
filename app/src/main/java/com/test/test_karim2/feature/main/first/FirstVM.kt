package com.test.test_karim2.feature.main.first

import android.content.Context
import androidx.lifecycle.*
import com.chareem.core.BaseVM
import com.chareem.core.data.BaseResponse
import com.chareem.core.util.NetworkUtil
import com.test.test_karim2.R
import com.test.test_karim2.Repository.globalRepository
import com.test.test_karim2.data.model.Book
import com.test.test_karim2.data.model.BookReport
import com.test.test_karim2.data.model.BookStokRelation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FirstVM(private val userRepository: globalRepository): BaseVM() {
    override fun getTagName(): String = javaClass.simpleName

    private val _get = MutableLiveData<BaseResponse<List<BookStokRelation>>>()
    val searchBook: LiveData<BaseResponse<List<BookStokRelation>>> = _get

    fun serchBook(tittle: String, context: Context){
        viewModelScope.launch {
            kotlin.runCatching {
                _get.value = BaseResponse.Loading()
                userRepository.searchBook(tittle)
            }.onSuccess { data ->
                data.let {
                  it.collect { it2  ->
                      _get.postValue(BaseResponse.Success(it2))
                  }
                }
            }.onFailure {
                if (NetworkUtil.isNetworkConnected(context))
                    _get.postValue(BaseResponse.Error(it, 2))
                else _get.postValue(BaseResponse.Error(it, 2, context.getString(R.string.error_connection)))
            }

        }
    }

    fun addStok(film: Book, stok: Int){
        viewModelScope.launch {
            kotlin.runCatching {
                userRepository.addStok(film, stok)
            }.onSuccess { data ->

            }.onFailure {
            }
        }
    }

    fun minusStok(film: Book, stok: Int){
        viewModelScope.launch {
            kotlin.runCatching {
                userRepository.minusStok(film, stok)
            }.onSuccess { data ->

            }.onFailure {
            }
        }
    }

    suspend fun getReport(book_id: String): BookReport {
        return userRepository.getReport(book_id)
    }
}
