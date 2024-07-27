package com.project.how.data_class.dto.schedule

import com.google.gson.annotations.SerializedName

typealias GetScheduleListResponse = List<GetScheduleListResponseElement>

data class GetScheduleListResponseElement (
    @SerializedName("id") val id: Long,
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("totalPrice") val totalPrice: Long,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("country") val country : String,
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("longitude") val longitude : Double
)
