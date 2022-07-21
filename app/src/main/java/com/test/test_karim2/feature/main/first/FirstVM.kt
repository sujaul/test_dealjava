package com.test.test_karim2.feature.main.first

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.chareem.core.BaseVM
import com.chareem.core.data.BaseResponse
import com.chareem.core.util.NetworkUtil
import com.test.test_karim2.R
import com.test.test_karim2.Repository.globalRepository
import com.test.test_karim2.data.model.Film
import com.test.test_karim2.data.model.FilmAndFilmstokRelation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FirstVM(private val userRepository: globalRepository): BaseVM() {
    override fun getTagName(): String = javaClass.simpleName

    private val _get = MutableLiveData<BaseResponse<List<FilmAndFilmstokRelation>>>()
    val searchFilm: LiveData<BaseResponse<List<FilmAndFilmstokRelation>>> = _get

    fun serchFilm(genre: String, context: Context){
        viewModelScope.launch {
            kotlin.runCatching {
                _get.value = BaseResponse.Loading()
                userRepository.searchFilm(genre)
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
}
