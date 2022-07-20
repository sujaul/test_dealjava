package com.test.test_karim2.feature.main.fourth

import androidx.lifecycle.*
import com.chareem.core.BaseVM
import com.chareem.core.data.BaseResponse
import com.test.test_karim2.Repository.globalRepository
import com.test.test_karim2.data.model.Users
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FourthVM(private val userRepository: globalRepository): BaseVM() {
    override fun getTagName(): String = javaClass.simpleName


    private val _get_locale = MutableLiveData<BaseResponse<List<Users>>>()
    val getUsers_locale: LiveData<BaseResponse<List<Users>>> = _get_locale
    fun getGuestToLocale(){
        viewModelScope.launch {
            kotlin.runCatching {
                _get_locale.value = BaseResponse.Loading()
                userRepository.getUserLocal()
            }.onSuccess { data ->
                data?.collect {
                    _get_locale.postValue(BaseResponse.Success(it))
                } ?: _get_locale.postValue(BaseResponse.Success(listOf()))
            }.onFailure {
                _get_locale.postValue(BaseResponse.Error(it, 2))
            }

        }
    }
}
