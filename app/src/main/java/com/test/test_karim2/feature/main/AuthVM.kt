package com.test.test_karim2.feature.main

import androidx.lifecycle.*
import com.galee.core.BaseVM
import com.galee.core.data.BaseResponse
import com.test.test_karim2.Repository.globalRepository
import com.test.test_karim2.data.model.Register
import com.test.test_karim2.data.model.ResponseParser
import com.test.test_karim2.data.model.Users
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthVM(private val userRepository: globalRepository): BaseVM() {
    override fun getTagName(): String = javaClass.simpleName

    private val _register_user = MutableLiveData<BaseResponse<ResponseParser>>()
    val registerUsers: LiveData<BaseResponse<ResponseParser>> = _register_user
    fun registerUser(reg: Register){
        viewModelScope.launch {
            kotlin.runCatching {
                _register_user.value = BaseResponse.Loading()
                userRepository.registerUser(reg)
            }.onSuccess { data ->
                _register_user.postValue(BaseResponse.Success(data))
            }.onFailure {
                _register_user.postValue(BaseResponse.Error(it, 2))
            }

        }
    }

    private val _login_user = MutableLiveData<BaseResponse<ResponseParser>>()
    val loginUsers: LiveData<BaseResponse<ResponseParser>> = _register_user
    fun loginUser(reg: Register){
        viewModelScope.launch {
            kotlin.runCatching {
                _register_user.value = BaseResponse.Loading()
                userRepository.registerUser(reg)
            }.onSuccess { data ->
                _register_user.postValue(BaseResponse.Success(data))
            }.onFailure {
                _register_user.postValue(BaseResponse.Error(it, 2))
            }

        }
    }

    private val _get_user= MutableLiveData<BaseResponse<List<Users>>>()
    val getUsers: LiveData<BaseResponse<List<Users>>> = _get_user
    fun getUser(username: String, password: String){
        viewModelScope.launch {
            kotlin.runCatching {
                _get_user.value = BaseResponse.Loading()
                userRepository.getUserByUsernameAndPass(username, password)
            }.onSuccess { data ->
                data.collect {
                    _get_user.postValue(BaseResponse.Success(it))
                }
            }.onFailure {
                _get_user.postValue(BaseResponse.Error(it, 2))
            }

        }
    }
}
