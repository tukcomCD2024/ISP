package com.project.how.network.api_interface

import com.project.how.data_class.dto.GenerateSkyscannerUrlRequest
import com.project.how.data_class.dto.GenerateSkyscannerUrlResponse
import com.project.how.data_class.dto.GetFlightOffersRequest
import com.project.how.data_class.dto.GetFlightOffersResponse
import com.project.how.data_class.dto.LikeFlightElement
import com.project.how.data_class.dto.LikeFlightList
import retrofit.http.Body
import retrofit.http.DELETE
import retrofit.http.GET
import retrofit.http.POST
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Path

interface BookingService {
    @GET("bookings/flights/search")
    fun getFlightOffers(
        @Header("Authorization") accessToken : String,
        @Body getFlightOffersRequest: GetFlightOffersRequest
    ) : Call<GetFlightOffersResponse>

    @POST("bookings/flights/connect")
    fun generateSkyscannerUrl(
        @Header("Authorization") accessToken : String,
        @Body generateSkyscannerUrlRequest: GenerateSkyscannerUrlRequest
    ) : Call<GenerateSkyscannerUrlResponse>

    @POST("bookings/flight/like")
    fun addLikeFlight(
        @Header("Authorization") accessToken : String,
        @Body addLikeFlightRequest: LikeFlightElement
    )

    @GET("bookings/flight/likes")
    fun getLikeFlight(
        @Header("Authorization") accessToken : String
    ) : Call<LikeFlightList>

    @DELETE("bookings/flight/likes/{id}")
    fun deleteLikeFlight(
        @Header("Authorization") accessToken : String,
        @Path("id", encoded = true) id : Long
    )
}