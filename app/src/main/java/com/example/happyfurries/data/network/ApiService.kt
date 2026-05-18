package com.example.happyfurries.data.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// Aquí defino todas las rutas del backend que voy a usar desde la app.
// Retrofit lee las anotaciones (@GET, @POST...) y genera solo el código
// de red, yo solo tengo que decirle qué quiero hacer y con qué datos.

interface ApiService {

    // ------ MASCOTAS ------

    // Pide todas las mascotas al servidor → GET /pets
    @GET("pets")
    suspend fun getPets(): List<PetApiModel>

    // Pide una mascota concreta por su ID → GET /pets/1
    // @Path sustituye {id} en la URL por el número real
    @GET("pets/{id}")
    suspend fun getPet(@Path("id") id: Int): PetApiModel

    // Crea una mascota nueva → POST /pets
    // @Body manda el objeto como JSON en el cuerpo de la petición
    @POST("pets")
    suspend fun createPet(@Body pet: PetApiModel): PetApiModel

    // Actualiza una mascota existente → PUT /pets/1
    @PUT("pets/{id}")
    suspend fun updatePet(@Path("id") id: Int, @Body pet: PetApiModel): PetApiModel

    // Borra una mascota → DELETE /pets/1
    @DELETE("pets/{id}")
    suspend fun deletePet(@Path("id") id: Int)

    // ------ EVENTOS ------

    // Pide eventos con filtros opcionales → GET /events?petId=1&date=2026-05-20
    // Si no paso petId o date, simplemente no se añaden a la URL
    @GET("events")
    suspend fun getEvents(
        @Query("petId") petId: Int? = null,
        @Query("date")  date: String? = null
    ): List<EventApiModel>

    // Crea un evento nuevo para una mascota → POST /events
    @POST("events")
    suspend fun createEvent(@Body event: EventApiModel): EventApiModel

    // Actualiza un evento existente → PUT /events/1
    @PUT("events/{id}")
    suspend fun updateEvent(@Path("id") id: Int, @Body event: EventApiModel): EventApiModel

    // Borra un evento → DELETE /events/1
    @DELETE("events/{id}")
    suspend fun deleteEvent(@Path("id") id: Int)
}