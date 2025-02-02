package com.example.metanitdatabase

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserViewModel(application: Application) : ViewModel() {
    val userList: LiveData<List<User>>
    private val repository: UserRepository
    var userName = mutableStateOf("")
    var userAge = mutableIntStateOf(0)

    init {
        // Строит базу данных (если она еще не существует)
        val userDb = UserRoomDatabase.getInstance(application)
        val userDao = userDb.userDao

        repository = UserRepository(userDao)
        userList = repository.userList
    }

    fun changeName(value: String) {
        userName.value = value
    }

    fun changeAge(value: String) {
        if (value == "") {
            return
        }

        userAge.intValue = value.toInt()
    }

    fun addUser() {
        repository.addUser(User(userName.value, userAge.value))
    }

    fun deleteUser(id: Int) {
        repository.deleteUser(id)
    }
}

class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(application) as T
    }
}