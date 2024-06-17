package com.project.how.network.api_interface

import com.project.how.data_class.dto.EmptyResponse
import com.project.how.data_class.dto.GenerateOneWaySkyscannerUrlRequest
import com.project.how.data_class.dto.GenerateSkyscannerUrlRequest
import com.project.how.data_class.dto.GenerateSkyscannerUrlResponse
import com.project.how.data_class.dto.GetFlightOffersRequest
import com.project.how.data_class.dto.GetFlightOffersResponse
import com.project.how.data_class.dto.GetLikeFlightResponse
import com.project.how.data_class.dto.GetOneWayFlightOffersRequest
import com.project.how.data_class.dto.GetOneWayFlightOffersResponse
import com.project.how.data_class.dto.LikeFlightElement
import com.project.how.data_class.dto.LikeFlight
import com.project.how.data_class.dto.LikeOneWayFlightElement
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Path

interface BookingService {
    @POST("bookings/flights/search")
    fun getFlightOffers(
        @Header("Authorization") accessToken : String,
        @Body getFlightOffersRequest: GetFlightOffersRequest
    ) : Call<GetFlightOffersResponse>

    @POST("bookings/flights/search")
    fun getOneWayFligthOffers(
        @Header("Authorization") accessToken : String,
        @Body getOneWayFlightOffersRequest: GetOneWayFlightOffersRequest
    ) : Call<GetOneWayFlightOffersResponse>

    @POST("bookings/flights/connect")
    fun generateSkyscannerUrl(
        @Header("Authorization") accessToken : String,
        @Body generateSkyscannerUrlRequest: GenerateSkyscannerUrlRequest
    ) : Call<GenerateSkyscannerUrlResponse>

    @POST("bookings/flights/connect")
    fun generateOneWaySkyscannerUrl(
        @Header("Authorization") accessToken : String,
        @Body generateOneWaySkyscannerUrlRequest: GenerateOneWaySkyscannerUrlRequest
    ) : Call<GenerateSkyscannerUrlResponse>

    @POST("bookings/flights/like")
    fun addLikeFlight(
        @Header("Authorization") accessToken : String,
        @Body addLikeFlightRequest: LikeFlightElement
    ) : Call<EmptyResponse>

    @POST("bookings/flights/like")
    fun addLikeOneWayFlight(
        @Header("Authorization") accessToken : String,
        @Body addLikeFlightRequest: LikeOneWayFlightElement
    ) : Call<EmptyResponse>

    @GET("bookings/flights/likes")
    fun getLikeFlight(
        @Header("Authorization") accessToken : String
    ) : Call<GetLikeFlightResponse>

    @DELETE("bookings/flights/likes/{id}")
    fun deleteLikeFlight(
        @Header("Authorization") accessToken : String,
        @Path("id", encoded = true) id : Long
    )
}