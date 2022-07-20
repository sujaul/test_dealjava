package com.test.test_karim2.feature.main.third

import androidx.lifecycle.*
import com.galee.core.BaseVM
import com.galee.core.data.BaseResponse
import com.test.test_karim2.Repository.globalRepository
import com.test.test_karim2.data.model.Persons
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ThirdVM(private val userRepository: globalRepository): BaseVM() {
    override fun getTagName(): String = javaClass.simpleName

    private val _get = MutableLiveData<BaseResponse<List<Persons>>>()
    val getUsers: LiveData<BaseResponse<List<Persons>>> = _get

    fun getPersons(page: Int, per_page: Int){
        viewModelScope.launch {
            kotlin.runCatching {
                _get.value = BaseResponse.Loading()
                userRepository.getUserToApi(page, per_page)
            }.onSuccess { data ->
                data.let {
                  it.collect { it2  ->
                      _get.postValue(BaseResponse.Success(it2))
                  }
                }
            }.onFailure {
                _get.postValue(BaseResponse.Error(it, 2))
            }

        }
    }
}
