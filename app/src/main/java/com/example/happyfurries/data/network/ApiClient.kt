package com.example.happyfurries.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Configuración de Retrofit para toda la app.
// Object (Singleton) -> solo exista una instancia,


object ApiClient {

    // 10.0.2.2 IP que usa el emulador para llegar a localhost del PC.

    private const val BASE_URL = "http://10.0.2.2:3000/"

    // Servicio con lazy para que no se inicialice hasta que lo necesite.
    // GsonConverterFactory convierte automáticamente JSON -> objetos Kotlin.
    val service: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}