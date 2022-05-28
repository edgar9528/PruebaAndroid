package com.gs.pruebaandroid.interfaces

import com.gs.pruebaandroid.model.PeliculaResponse
import com.gs.pruebaandroid.model.VideosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    fun getPeliculas(@Url url:String): Call<PeliculaResponse>

    @GET
    fun getVideos(@Url url:String): Call<VideosResponse>
}