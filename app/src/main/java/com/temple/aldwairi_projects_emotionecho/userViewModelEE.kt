package com.temple.aldwairi_projects_emotionecho
import androidx.compose.runtime.mutableStateOf


class userViewModelEE {
    private var currentUser = mutableStateOf(User())

    fun getCurrentUser(): User{
        return  currentUser.value
    }

    fun setCurrentUser(user: User){
        currentUser.value = user
    }

    companion object{
        val TAG = "UserViewModel"
    }

}