package com.example.happyfurries.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Configuración de Retrofit para toda la app.
// Lo defino como object (Singleton) para que solo exista una instancia,
// no quiero crear una conexión nueva cada vez que abro una pantalla.

object ApiClient {

    // 10.0.2.2 es la IP que usa el emulador para llegar a localhost de mi ordenador.
    // Si usara un móvil físico tendría que poner la IP de mi PC en la red local.
    private const val BASE_URL = "http://10.0.2.2:3000/"

    // Creo el servicio con lazy para que no se inicialice hasta que lo necesite.
    // GsonConverterFactory es el que convierte automáticamente JSON -> objetos Kotlin.
    val service: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}