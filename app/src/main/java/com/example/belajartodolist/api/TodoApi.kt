package com.example.belajartodolist.api

import retrofit2.Response
import retrofit2.http.*

public interface TodoApi {
    @GET("/rest/v1/todo?select=*&order=id.asc")
    suspend fun get(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String
    ) : Response<List<TodoItem>>

    @POST("/rest/v1/todo")
    suspend fun create(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
        @Body todoData: TodoData
    )

    @PATCH("/rest/v1/todo")
    suspend  fun update(
        @Header("Authorization") token: String,
        @Header("apikey") apiKey: String,
        @Query("id") idQuery : String,
        @Body todoData: TodoData
    ) : Response<Unit>

    @DELETE("/rest/v1/todo")
    suspend fun delete(
        @Header("Authorization") token: String,
        @Header ("apikey") apiKey: String,
        @Query ("id") idQuery : String,
    ) : Response<Unit>
}