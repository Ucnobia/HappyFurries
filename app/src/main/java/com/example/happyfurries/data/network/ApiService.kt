package com.example.happyfurries.data.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// Rutas del backend desde la app.
// Retrofit lee  @GET, @POST etc --> genera solo el código de red
// Solo le paso que quiero hacer y con qué datos.

interface ApiService {

    //  MASCOTAS

    // Pide todas las mascotas  → GET /pets
    @GET("pets")
    suspend fun getPets(): List<PetApiModel>

    // Pide mascota concreta por su ID → GET /pets/1
    // @Path sustituye {id} en la URL por el número real
    @GET("pets/{id}")
    suspend fun getPet(@Path("id") id: Int): PetApiModel

    // Crea  mascota nueva → POST /pets
    // @Body manda el objeto como JSON en el cuerpo de la petición
    @POST("pets")
    suspend fun createPet(@Body pet: PetApiModel): PetApiModel

    // Actualiza mascota existente → PUT /pets/1
    @PUT("pets/{id}")
    suspend fun updatePet(@Path("id") id: Int, @Body pet: PetApiModel): PetApiModel


    //  EVENTOS

    // Pide eventos con filtros opcionales → GET /events?petId=1&date=2026-05-20
    // NO petId o date, NO se añade a la URL
    @GET("events")
    suspend fun getEvents(
        @Query("petId") petId: Int? = null,
        @Query("date")  date: String? = null
    ): List<EventApiModel>

    // Crea un evento nuevo para una mascota → POST /events
    @POST("events")
    suspend fun createEvent(@Body event: EventApiModel): EventApiModel

    // Borra un evento → DELETE /events/1
    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id: Int)
}