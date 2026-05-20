package com.example.happyfurries.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.data.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel de mascotas — gestiona los datos que ve la UI.
// Usa viewModelScope para lanzar corrutinas, así las operaciones
// de red y base de datos no bloquean el hilo principal.

class PetViewModel(
    private val petRepository: PetRepository
) : ViewModel() {

    // Lista de mascotas que observan las pantallas.
    // MutableStateFlow es el que yo modifico internamente,
    // StateFlow es el que expongo a la UI (solo lectura).
    private val _pets = MutableStateFlow<List<PetEntity>>(emptyList())
    val pets: StateFlow<List<PetEntity>> = _pets

    // Carga todas las mascotas desde el repositorio
    // (que a su vez sincroniza con el backend y Room)
    fun loadPets() {
        viewModelScope.launch {
            _pets.value = petRepository.getAllPets()
        }
    }

    // Añade una mascota nueva y recarga la lista
    // onFinished es opcional, lo uso para navegar atrás al terminar
    fun addPet(pet: PetEntity, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            petRepository.insertPet(pet)
            loadPets()
            onFinished?.invoke()
        }
    }

    // Actualiza los datos de una mascota existente y recarga la lista
    fun updatePet(pet: PetEntity, onFinished: (() -> Unit)? = null) {
        viewModelScope.launch {
            petRepository.updatePet(pet)
            loadPets()
            onFinished?.invoke()
        }
    }
}