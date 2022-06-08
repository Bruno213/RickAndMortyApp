package com.example.rickandmortyapi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapi.Repositories.Repository
import com.example.rickandmortyapi.model.CharacterList
import com.example.rickandmortyapi.ui.listfragment.CharacterAdapter
import kotlinx.coroutines.launch
import retrofit2.Response

class SharedViewModel(
    private val repository: Repository
): ViewModel() {

    val listCharacters = MutableLiveData<Response<CharacterList>?>()
    val listCharactersFirstPage = MutableLiveData< Response<CharacterList> >()

    val filterValue = MutableLiveData<Array<Int>>()
    var isFilter = MutableLiveData<Boolean>()
    var filterOnFirstTime = false

    var currentPage = 1
    var pages = 1

    init {
        filterValue.value = arrayOf(0, 0)
        isFilter.value = false
    }


    fun getCharacters(page: Int) {
        viewModelScope.launch {
            val characters = repository.getCharacters(page)
            listCharacters.value = characters
            isFilter.value = false
        }
    }

    fun getFirstPage() {
        viewModelScope.launch {
            val characters = repository.getCharacters(1)
            listCharactersFirstPage.value = characters
            isFilter.value = false
        }
    }

    fun getCharactersByStatusAndGender(status: String, gender: String, page: Int) {
        viewModelScope.launch {
            val characters = repository.getCharactersByStatusAndGender(status, gender, page)

            listCharacters.value = characters
            isFilter.value = true
        }
    }

    fun getCharactersByStatus(status: String, page: Int) {
        viewModelScope.launch {
            val characters = repository.getCharactersByStatus(status, page)

            listCharacters.value = characters
            isFilter.value = true
        }
    }

    fun getCharactersByGender(gender: String, page: Int) {
        viewModelScope.launch {
            val characters = repository.getCharactersByGender(gender, page)

            listCharacters.value = characters
            isFilter.value = true
        }
    }

    fun clearList() {
        listCharacters.value = null
    }

    fun increasePageCount() {
        currentPage++
    }

    fun getCurrentPageList(): Int {
        return currentPage
    }
}