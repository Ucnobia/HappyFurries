package com.example.happyfurries.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PetViewModel(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _pets = MutableStateFlow<List<PetEntity>>(emptyList())
    val pets: StateFlow<List<PetEntity>> = _pets

    fun loadPets() {
        viewModelScope.launch {
            _pets.value = petRepository.getAllPets()
        }
    }

    fun addPet(pet: PetEntity, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            petRepository.insertPet(pet)
            loadPets()
            onFinished?.invoke()
        }
    }
}
