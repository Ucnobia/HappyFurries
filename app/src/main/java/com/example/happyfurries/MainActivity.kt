package com.example.happyfurries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.happyfurries.data.database.DatabaseProvider
import com.example.happyfurries.data.repository.EventRepository
import com.example.happyfurries.data.repository.PetRepository
import com.example.happyfurries.navigation.AppNavHost
import com.example.happyfurries.ui.theme.HappyFurriesTheme
import com.example.happyfurries.ui.viewmodel.EventViewModel
import com.example.happyfurries.ui.viewmodel.PetViewModel

class MainActivity : ComponentActivity() {

    private lateinit var petRepository: PetRepository
    private lateinit var petViewModel: PetViewModel

    private lateinit var eventRepository: EventRepository
    private lateinit var eventViewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar Room
        val db = DatabaseProvider.getDatabase(this)

        // Repositorios
        petRepository = PetRepository(db.petDao())
        eventRepository = EventRepository(db.eventDao())

        // ViewModels
        petViewModel = PetViewModel(petRepository)
        eventViewModel = EventViewModel(eventRepository)

        // UI
        setContent {
            HappyFurriesTheme {
                val navController = rememberNavController()

                AppNavHost(
                    navController = navController,
                    petViewModel = petViewModel,
                    eventViewModel = eventViewModel
                )
            }
        }
    }
}
